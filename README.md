log-monitoring
==============

Small Java program to monitor a log file and send an e-mail when a new line in the log file matches a regex.

Usage
-----
In order for the program to run, you need to specify the following parameters as command line arguments:
* Input file (-i)
* Regex to use on every line (-m)
* SMTP mail server to use (-h)
* Recipient e-mail address (-r)
* Sender e-mail address (-s)


Example
-------
> java -jar log-monitoring.jar -i test.log -m ".\*ERROR.\*|.\*WARN.\*" -h mail.krenger.local -s logsupervisor@krenger.local -r simon@krenger.local


This example will monitor the log file **test.log** and test each new line against the regex **".\*ERROR.\*|.\*WARN.\*"** (just line tail on UNIX). If the line matches the regex, an e-mail will be sent to **simon@krenger.local** via the SMTP server **mail.krenger.local** and with the sender address **logsupervisor@krenger.local**.
