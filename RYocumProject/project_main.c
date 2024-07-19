/**
 * The main entry point of the thermocouple simulation program.
 *
 * This program simulates a thermocouple as a deamon process.
 * It is designed to be started at system startup in the init
 * (or alternative) hierarchy under linux. This module contains
 * the entry point (main()) at the bottom of the file. All
 * other functions are module private functions (i.e. static),
 * and not part of the exposed API. The only exposed function
 * is main().
 */
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <signal.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <syslog.h>
#include <stdbool.h>
#include <curl/curl.h>
#include <string.h>
#include <argp.h>

#include "tc_error.h"
#include "tc_state.h"

#define NO_ARG 0
#define OK 0
#define ERR_WTF 9
#define REQ_ERR 11

static const char *DAEMON_NAME = "tcsimd";
static const char *TEMP_FILENAME = "/tmp/temp";
static const char *STATE_FILENAME = "/tmp/status";
static const char *WORKING_DIR = "/";

static const long SLEEP_DELAY = 5;
static float temp = 64;

// params, move to config file
static const char *STATE_URL = "http://18.217.90.61:8080/status";
static const char *TEMP_URL = "http://18.217.90.61:8080/temp";

// set params for argp
static char args_doc[] = "--post --url http://localhost:8000 'argument'\n-o -u http://localhost:8000 'argument'";
static char doc[] = "Always provide a url or valid http for each command";

// arguments will be used for storing values from command line
struct Arguments
{
    char *arg; // for string argument
    char *url;
    bool post;
    bool get;
    bool put;
    bool delete;
};

struct Curlmem
{
    char *response;
    size_t size;
};

struct Curlmem chunk = {0};

// argp options required for output to user
static struct argp_option options[] = {
    {"url", 'u', "valid http", NO_ARG, "URL for the requested HTTP, Mandatory"},
    {"post", 'o', NO_ARG, NO_ARG, "POST HTTP Request, requires a verb"},
    {"get", 'g', NO_ARG, NO_ARG, "GET HTTP Request"},
    {"put", 'p', NO_ARG, NO_ARG, "PUT HTTP Request, requires a verb"},
    {"delete", 'd', NO_ARG, NO_ARG, "DELETE HTTP Request, requires a verb"},
    {NO_ARG}};

static size_t call_back(void *data, size_t size, size_t nmemb, void *userp)
{
    size_t realsize = size * nmemb;
    struct Curlmem *mem = (struct Curlmem *)userp;

    char *ptr = realloc(mem->response, mem->size + realsize + 1);
    if (ptr == NULL)
    {
        return 0;
    }

    mem->response = ptr;
    memcpy(&(mem->response[mem->size]), data, realsize);
    mem->size += realsize;
    mem->response[mem->size] = 0;

    return realsize;
}

static char *send_http_request(char *url, char *message, char *type, bool verb)
{
    CURL *curl = curl_easy_init();
    if (curl)
    {
        CURLcode res;
        FILE *outputFile = fopen("curloutput.txt", "wb");
        curl_easy_setopt(curl, CURLOPT_URL, url);
        curl_easy_setopt(curl, CURLOPT_CUSTOMREQUEST, type);
        curl_easy_setopt(curl, CURLOPT_WRITEDATA, outputFile);

        if (strcmp(type, "GET") == 0)
        {
            curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, call_back);
            curl_easy_setopt(curl, CURLOPT_WRITEDATA, (void *)&chunk);
        }

        if (verb)
        {
            curl_easy_setopt(curl, CURLOPT_POSTFIELDS, message);
        }
        else
        {
            curl_easy_setopt(curl, CURLOPT_FOLLOWLOCATION, 1L);
        }
        res = curl_easy_perform(curl);

        if (res != CURLE_OK)
        {
            return REQ_ERR;
        }

        curl_easy_cleanup(curl);
    }
    else
    {
        return NULL;
    }
    return chunk.response;
}

int handle_requirement_error(char *message, struct argp_state *state)
{
    argp_usage(state);
    return REQ_ERR;
}

// parse command line options IF not run as a daemon instance
static error_t parse_opt(int key, char *arg, struct argp_state *state)
{
    struct Arguments *arguments = state->input;
    switch (key)
    {
    case 'u':
        arguments->url = arg;
        break;
    case 'o':
        arguments->post = true;
        break;
    case 'g':
        arguments->get = true;
        break;
    case 'p':
        arguments->put = true;
        break;
    case 'd':
        arguments->delete = true;
        break;
    case ARGP_KEY_NO_ARGS:
        // check if args are required based on request type, notify user
        if (arguments->post == true || arguments->put == true || arguments->delete == true)
        {
            return handle_requirement_error("You need to supply a VERB.\n", state);
        }
    case ARGP_KEY_ARG:
        // if too many arguments are given, notify user
        if (state->arg_num >= 1)
        {
            printf("Too many arguments: Provide a quote (example:'hello friend') \n");
            argp_usage(state);
            return REQ_ERR;
        }
        arguments->arg = arg;
        break;
    case ARGP_KEY_END:
        // if url is null or malformed, notify user
        if (arguments->url == NULL)
        {
            printf("Please provide a valid url. For example: http://localhost:8080 \n");
            argp_usage(state);
            return REQ_ERR;
        }
        else if (arguments->get == false && arguments->post == false && arguments->put == false && arguments->delete == false)
        {
            return handle_requirement_error("You must select http request type.\n", state);
        }
        break;
    case ARGP_KEY_SUCCESS:
        // perform request based on type, should this be limited to only one type allowed...
        if (arguments->get)
        {
            send_http_request(arguments->url, NULL, "GET", false);
            break;
        }
        else if (arguments->post)
        {
            send_http_request(arguments->url, arguments->arg, "POST", true);
            break;
        }
        else if (arguments->put)
        {
            send_http_request(arguments->url, arguments->arg, "PUT", true);
            break;
        }
        else if (arguments->delete)
        {
            send_http_request(arguments->url, arguments->arg, "DELETE", true);
            break;
        }
        break;
    default:
        return ARGP_ERR_UNKNOWN;
    }
    return OK;
}

static int write_state(char *state)
{
    FILE *fp = fopen(STATE_FILENAME, "w");
    if (fp == NULL)
    {
        printf("unable to open file for writing\n");
        return ERR_WTF;
    }
    fputs(state, fp);
    fclose(fp);
    return OK;
}

static void handle_state()
{
    char *state = send_http_request(STATE_URL, NULL, "GET", false);    
    if (state == ON || strcmp(state, "ON") == 0 || state == 0)
    {
        write_state("ON");
        sleep(SLEEP_DELAY);
        send_http_request(STATE_URL, "ON", "POST", true);        
    }
    else if (state == OFF || state == "OFF" || state == 1)
    {
        write_state("OFF");
        sleep(SLEEP_DELAY);
        send_http_request(STATE_URL, "OFF", "POST", true);        
    }
    else
    {
        write_state("ON");
        sleep(SLEEP_DELAY);
        send_http_request(STATE_URL, "ON", "POST", true);        
    }

    chunk.response = NULL;
    chunk.size = NULL;
}

static struct argp argp = {options, parse_opt, args_doc, doc};

/**
 * If we exit the process, we want to sent information on
 * the reason for the exit to syslog, and then close
 * the log. This is a way for us to centralize cleanup
 * when we leave the daemon process.rg
 *
 * @param err The error code we exit under.
 */
static void _exit_process(const tc_error_t err)
{
    syslog(LOG_INFO, "%s", tc_error_to_msg(err));
    closelog();
    exit(err);
}

/**
 * This is the signal hander we set on the daemon
 * process after initialization. This way, we can
 * intercept and handle signals from the OS.
 *
 * @param signal The signal from the OS.
 */
static void _signal_handler(const int signal)
{
    switch (signal)
    {
    case SIGHUP:
        break;
    case SIGTERM:
        _exit_process(RECV_SIGTERM);
        break;
    default:
        syslog(LOG_INFO, "received unhandled signal");
    }
}

/**
 * When we start a daemon process, we need to fork from the
 * parent so we can appropriately configure the process
 * as a standalone, daemon process with approrpiate stdin,
 * stdout, and the like. Here, we handle errors if we are
 * unable to fork or we are the parent process and the fork
 * worked. If the fork failed, we record that and exit.
 * Otherwise, we exit the parent cleanly.
 *
 * @param pid The process ID of th enew process.
 */
static void _handle_fork(const pid_t pid)
{
    // For some reason, we were unable to fork.
    if (pid < 0)
    {
        _exit_process(NO_FORK);
    }

    // Fork was successful so exit the parent process.
    if (pid > 0)
    {
        exit(OK);
    }
}

/**
 * Here, we handle the details of daemonizing a process.
 * This involves forking, opening the syslog connection,
 * configuring signal handling, and closing standard file
 * descriptors.
 */
static void _daemonize(void)
{
    // Fork from the parent process.
    pid_t pid = fork();

    // Open syslog with the specified logmask.
    openlog(DAEMON_NAME, LOG_PID | LOG_NDELAY | LOG_NOWAIT, LOG_DAEMON);

    // Handle the results of the fork.
    _handle_fork(pid);

    // Now become the session leader.
    if (setsid() < -1)
    {
        _exit_process(NO_SETSID);
    }

    // Set our custom signal handling.
    signal(SIGTERM, _signal_handler);
    signal(SIGHUP, _signal_handler);

    // New file persmissions on this process, they need to be permissive.
    // umask(S_IRUSR | S_IWUSR | S_IRGRP | S_IWGRP | S_IROTH | S_IWOTH);
    // umask(666);

    // Change to the working directory.
    chdir(WORKING_DIR);

    // Closing file descriptors (STDIN, STDOUT, etc.).
    for (long x = sysconf(_SC_OPEN_MAX); x >= 0; x--)
    {
        close(x);
    }
}

/**
 * This runs the simulation. We essentially have a loop which
 * reads the heater state, adjust the temperature based on this
 * information, and writes the new temperature to the appropriate
 * location.
 */
static void _run_simulation(void)
{

    // It's a bit cold! Note we're using a float in case we want to be
    // more sophisticated with the temperature management in the future.
    // Right now we just use a linear model.
    tc_heater_state_t heater_state = OFF;
    syslog(LOG_INFO, "beginning thermocouple simulation");
    while (true)
    {
        handle_state();
                // Read the heater state.
        // send_http_request(STATE_URL, &heater_state, "POST", true);
        tc_error_t err = tc_read_state(STATE_FILENAME, &heater_state);
        if (err != OK)
            _exit_process(err);

        // Is the heater on? then increase the temperature one degree.
        // Otherwise, it's getting colder!
        temp = (heater_state == ON) ? temp + 1 : temp - 1;
        char buffer[255];

        // converts float to string and storing it to the variable buffer
        gcvt(temp, 6, buffer);

        // Write the temp to the file.
        err = tc_write_temperature(TEMP_FILENAME, temp);
        send_http_request(TEMP_URL, buffer, "POST", true);
        if (err != OK)
            _exit_process(err);

        // Take a bit of a nap.
        sleep(SLEEP_DELAY);
    }
}

/**
 * A utility function to test for file existance.
 *
 * @param filename The name of the file to check.
 */
static bool _file_exists(const char *filename)
{
    struct stat buffer;
    return (stat(filename, &buffer) == 0) ? true : false;
}

/**
 * A utility function to create a file.
 *
 * @param name The name of the file to create.
 */
static void _create_file(const char *name)
{
    FILE *fp = fopen(name, "w");
    if (fp == NULL)
    {
        _exit_process(NO_OPEN);
    }
    fclose(fp);
}

/**
 * When we first start up, the various files we need to
 * use may not exist. If that is the case, we create them
 * here for future use.
 */
static void _configure(void)
{
    if (!_file_exists(STATE_FILENAME))
    {
        syslog(LOG_INFO, "no state file; creating.");
        _create_file(STATE_FILENAME);
    }

    if (!_file_exists(TEMP_FILENAME))
    {
        syslog(LOG_INFO, "no temp file; creating.");
        _create_file(TEMP_FILENAME);
    }
    syslog(LOG_INFO, "test finished.");
}

/**
 * The daemon entry point.
 */
int main(int argc, char **argv)
{

    int err;
    if (argc > 1)
    {
        // default arguments, which could be done in struct
        syslog(LOG_INFO, "Using command line rather than daemon script.");
        struct Arguments arguments;
        arguments.url = NULL;
        arguments.arg = NULL;
        arguments.post = false;
        arguments.get = false;
        arguments.put = false;
        arguments.delete = false;

        // parse the arguments
        argp_parse(&argp, argc, argv, 0, 0, &arguments);
        // Daemonize the process.
    }
    else
    {
        _daemonize();
    }

    // Set up appropriate files if they don't exist.
    _configure();

    // Execute the primary daemon routines.
    _run_simulation();
    return ERR_WTF;

    // If we get here, something weird has happened.
    // return OK;
}
