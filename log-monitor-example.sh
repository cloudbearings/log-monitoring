#!/bin/bash
java -jar log-monitoring.jar -i test.log -m ".*ERROR.*|.*WARN.*" -h mail.krenger.local -s logsupervisor@krenger.local -r simon@krenger.local
