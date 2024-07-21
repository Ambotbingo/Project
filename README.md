# Project ECE 531 IoT
Ruvylene Yocum

_________________________ STATUS: http://18.217.90.61:8080/status___________________________________________________________________
http://18.217.90.61:8080/status
To POST for STATUS: curl -X POST http://18.217.90.61:8080/status
For ON Status:
curl -X POST http://18.217.90.61:8080/status -d ON
For OFF Status:
curl -X POST http://18.217.90.61:8080/status -d OFF
______________________________________________________________________________________________________________________



_________________________ SETTINGS: http://18.217.90.61:8080/settings___________________________________________________________________
http://18.217.90.61:8080/settings
temp1 must be lower than temp2
temp2 must be higher thatntemp1
To POST for STATUS: curl -X POST http://18.217.90.61:8080/settings <id>,<temp1>,<temp2>,<timeofday>
FOR ADDING SETTINGS:
curl -X POST http://18.217.90.61:8080/settings -d 1,70,75,MORNING
FOR DELETE SETTINGS: curl -X DELETE http://18.217.90.61:8080/settings/<id>
curl -X DELETE http://18.217.90.61:8080/settings/1 
______________________________________________________________________________________________________________________





_________________________ TEMP: http://18.217.90.61:8080/temp___________________________________________________________________
http://18.217.90.61:8080/temp
To POST for Settings: curl -X POST http://18.217.90.61:8080/temp




_________________________ TEMP: http://18.217.90.61:8080/report___________________________________________________________________
http://18.217.90.61:8080/report
To GET for Report: curl -X POST http://18.217.90.61:8080/report




///////MYSQL CREATED TABLE////////////////////////////////

create table temp ( id int AUTO_INCREMENT PRIMARY KEY, temp FLOAT(32,2), timeDateInfo DATETIME DEFAULT CURRENT_TIMESTAMP)
create table state ( state VARCHAR(5), timeDateInfo DATETIME DEFAULT CURRENT_TIMESTAMP);
mysql> create table settings (id int PRIMARY KEY, temp1 FLOAT (32,2), temp2 FLOAT(32,2), timeofday VARCHAR(250)) 
mysql> insert into settings (id, temp1, temp2, timeofday) values (1, 67,72,'MORNING);


_____________________________COMMANDS__________________________________
To display temp in log using qemu: cat /var/log/temp
To display status in log using qemu: cat /var/log/status
To display messages in log using qemu: cat /var/log/messages
To kill the simulation: killall <name>
Example:killall tcsimd



_____________TO Kill JAVA SERVER NANOHTTPD____________
netstat -plten | grep java
kill -9 <java id>




