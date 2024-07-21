package com.ryocum;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Response;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Timestamp;

public class AppServer extends NanoHTTPD {

    public AppServer() throws IOException {
        super(8080);
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println(
                "\nCongratulations your server is now running!\n Please open your browser to http://18.217.90.61:8080/ \n");
    }

    public static void main(String[] args) {
        try {
            // create new instance of server
            new AppServer();
        } catch (IOException ioe) {
            System.err.println("Something went wrong. an cannot connect to the server:\n" + ioe);
        }
    }

    @Override
    public Response serve(IHTTPSession session) {
        if (session.getMethod() == Method.GET) {
            return HTTPUtility.performGet(session);
        } else if (session.getMethod() == Method.POST) {
            return HTTPUtility.performPost(session);
        } else if (session.getMethod() == Method.PUT) {
            return HTTPUtility.performPost(session);
        } else if (session.getMethod() == Method.DELETE) {
            return HTTPUtility.performDelete(session);
        }

        return HTTPUtility.failedAttempt("unacceptable request type");
    }
}
