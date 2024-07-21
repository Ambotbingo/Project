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

///////MYSQL CREATED TABLE////////////////////////////////

create table temp ( id int AUTO_INCREMENT PRIMARY KEY, temp FLOAT(32,2), timeDateInfo DATETIME DEFAULT CURRENT_TIMESTAMP)
create table state ( state VARCHAR(5), timeDateInfo DATETIME DEFAULT CURRENT_TIMESTAMP);
mysql> create table settings (id int PRIMARY KEY, temp1 FLOAT (32,2), temp2 FLOAT(32,2), timeofday VARCHAR(250)) 
mysql> insert into settings (id, temp1, temp2, timeofday) values (1, 67,72,'MORNING);



