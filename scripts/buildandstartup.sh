#!/bin/bash

echo "Move to git folder"
cd /data/_dev/saatya/SAATYA/SibLinksService

echo "Pull from git"
#git pull

echo "Running maven install"
mvn clean install

echo "Copy to service folder"
cp target/siblinks-ws-0.1.0.jar /home/siblinks/saatyaserver

echo "Move to service folder"
cd /home/siblinks/saatyaserver

echo "Run server"
nohup java -Xms256M -Xmx512M -cp siblinks-ws-0.1.0.jar -Dserver=siblinks-server -Dcom.sun.management.jmxremote.port=3115 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -Djava.rmi.server.hostname=hostname -Dcom.sun.management.jmxremote com.siblinks.ws.service.Application --server.port=8181  > log-siblinks.log &

PID=$!

echo $PID > pid.txt

exit 0
