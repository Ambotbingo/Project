# IoT_Bing
Assignment: Communication in the Cloud
 This assignment will familiarize you with the tools you need to build the cloud portion of the final project. When finished, give me a URL I can use to submit a request to see if it is correctly handled, and explain your interface to me so I can use it. I WILL send malformed requests, so harden your interface so it won't crash when I do. I'll need to be able to verify that I can submit data (POST or POST and PUT), view data (GET), and delete data (DELETE).

As a review, I can use POST to both update and initially add data, or POST to update data and PUT to initially create data. Or you could support both. I need to receive an identifier for the created data item that I can then use with GET to view the data, and then DELETE to delete the data. I'll need to understand your URI scheme as well, so please include that with base URLs so I understand how to use your interface. Something like this would work:

http://<ip or hostname>/ (for PUT or POST requests)

http://<ip or hostname>/<id> (for GET requests)

http://<ip or hostname>/<id> (for DELETE requests)

Here, if I PUT or POST to the base URI, I create a new object. If I GET from the base URI with an appended ID, I get that data associated with that ID. If I DELETE from the base URI with an appended ID, I delete that record. Frequently with this kind of scheme a GET to the base URI would result in a data listing, but I'm not requiring that. The ability to create, read, update, and delete information is key.


NOTE:
The server is running in Nanohttd:
• Nanohttd: https://github.com/NanoHttpd/nanohttpd Small, easily embeddable HTTP server in Java.
Refer to  https://github.com/NanoHttpd/nanohttpd for the requirements for this server:

FOR THIS ASSIGNMENT TO PERFORM REQUEST, PLEASE SEE INSTRUCTIONS BELOW:

To PUT:
curl -X PUT http://18.217.90.61:8080/ -d <name>

To POST:
curl -X POST http://18.217.90.61:8080/ -d <name>

To DELETE: A specified id must be provided to delete
curl -X DELETE http://18.217.90.61:8080/<id>

To GET all in the mysql table:
curl http://18.217.90.61:8080/

To GET: Display a specific id
curl http://18.217.90.61:8080/<id>
