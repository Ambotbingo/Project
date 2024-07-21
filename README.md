# Project


To POST for STATUS: curl -X POST http://18.217.90.61:8080/status
For ON Status:
curl -X POST http://18.217.90.61:8080/status -d ON
For OFF Status:
curl -X POST http://18.217.90.61:8080/status -d OFF


To POST for Settings: curl -X POST http://18.217.90.61:8080/settings
For ON Status:
NOTE: for ID 1 in settings = MORNING
NOTE: for ID 2 in settings = AFTERNOON
NOTE: for ID 3 in settings = EVENING

curl -X POST http://18.217.90.61:8080/status -d ON
For OFF Status:
curl -X POST http://18.217.90.61:8080/status -d OFF




To DELETE: A specified id must be provided to delete
curl -X DELETE http://18.217.90.61:8080/<id>

To GET all in the mysql table:
curl http://18.217.90.61:8080/

To GET: Display a specific id
curl http://18.217.90.61:8080/<id>


To PUT :
curl -X PUT http://18.217.90.61:8080/ -d <name>
