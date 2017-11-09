# PagerDuty Commenter

Simple lambda function to post a note to any PagerDuty incident at SPS. It is available for anyone to use.

Function is expecting the "event" to be in this format:
```
{
    "incidentID": "ID-XXXXX"
    "message": "This is a test message."
}

```

#### Components:
* Lambda Function

Reads from:
* credstash tables

Uses:
* [Credstash](https://github.com/fugue/credstash)

#### Usage

1) Pre-req: IAM Permission to invoke this lambda. (The arn is exported in the cloudformation stack)

To discover the ARN, here are shell and cloudformation examples:
```shell
$ aws cloudformation --region us-east-1 describe-stacks --stack-name dev-oaf-pd-commenter --query Stacks[0].Outputs[0].OutputValue --output text
arn:aws:lambda:us-east-1:056684691971:function:dev-oaf-pd-commenter-CommenterFunction-1K4CN3YN2ZPUN
```
(Note: This is an example ARN, do not assume it is correct based on this README)
```yaml
Mappings:
  EnvironmentMappings:
    dev:
      CommenterStack: dev-oaf-pd-router-function
Resources:
  <snip lambda boilerplate>
      Environment:
        Variables:
          PDCommenterArn:
            "Fn::ImportValue":
                "Fn::Join": [":", [!FindInMap [EnvironmentMappings, !Ref Environment, CommenterStack], "PDCommenterArn" ]]
```


2) Something like this code in your function:
```python
def pd_commenter(pd_key, message):
    lambdac = boto3.client('lambda')
    event = {
        "ticketID" : pd_key,
        "message" : message
    }
    response = lambdac.invoke(
        FunctionName=os.environ['PDCommenterArn'],
        InvocationType='Event', # async
        LogType='None',
        Payload=json.dumps(event)
        )
    return response
```

#### Deploy
`make deploy`
