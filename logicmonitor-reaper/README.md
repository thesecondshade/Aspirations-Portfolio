### LogicMonitor Reaper
Lambda function that runs every 4 hours to interact with LogicMonitor REST API and delete any servers that have been dead for longer then a day. The function reads from credstash tables to find a username and password and inserts them into a url that is used to access the REST API. The REST API supplies the function with a list of servers that includes information about such as their status, host id, name and how long they have had their current status. These details are tested as qualifiers in an if statement and if they pass (they have a certain ips and they have been dead for more than a day) the server is deleted. 

#### Components:
* Lambda Function

Reads from:
* credstash tables

Uses:
* [Credstash Python package](https://github.com/fugue/credstash)

#### Usage
Not intended to be used manually.

#### Architecture Diagram
Standalone lambda, no diagram
   
#### Deploy
`make environ="dev" deploy`

