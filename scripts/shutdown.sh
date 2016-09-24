#!/bin/bash

PID_FILE=pid.txt
if [ ! -e $PID_FILE ] ; then
	echo "Does not find any pid.txt file"
	echo "Stop!"
	exit 0
fi

pid=$(<$PID_FILE)

if [ -z $pid ] ; then
	echo "Empty process id"
	echo "Stop!"
	exit 0
fi
echo "Going to kill process id : "$pid
kill -9 $pid
#remove pid file
rm $PID_FILE

echo "Done"
exit 0
