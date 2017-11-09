### SOP-lookup

The `SOP-lookup` is a multi-component, serverless, service that is responsible for querying Confluence SOP's and writing to PagerDuty Incidents' notes with a related SOP. The mindful design of this is to only handle a few _simple_ things and limit responsibility.


##### Components:
* Lambda Function

Reads from:
* [pd-router-function](../pd-router-function/README.md) SNS Topics

Uses:
* [PD-Commenter](../pd-commenter/README.md) function


#### Deploy
`make deploy`

