#!/bin/bash

echo "Run server"

nohup java -Xms512M -Xmx4096M -cp siblinks-ws-0.1.0.jar -Dserver=siblinks-server -Dsib.env=prod -Dcom.sun.management.jmxremote.port=3115 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -Djava.rmi.server.hostname=hostname -Dcom.sun.management.jmxremote com.siblinks.ws.service.Application --server.port=8181 > log-siblinks.log &

#nohup java -Xms256M -Xmx512M -cp siblinks-ws-0.1.0.jar -Dserver=siblinks-server -Dcom.sun.management.jmxremote.port=3115 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -Djava.rmi.server.hostname=hostname -Dcom.sun.management.jmxremote com.siblinks.ws.service.Application --server.port=8181  > log-siblinks.log &
#nohup java -Xms256M -Xmx512M -cp siblinks-ws-0.1.0.jar -Dserver=siblinks-server -Dcom.sun.management.jmxremote.port=3116 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -Djava.rmi.server.hostname=hostname -Dcom.sun.management.jmxremote com.siblinks.ws.service.Application --server.port=8182  > log-siblinks.log &
PID=$!

echo $PID > pid.txt

exit 0
