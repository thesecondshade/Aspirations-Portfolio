"""
    This script is triggered when something is written to the PagerDutySNS, finds related SOPs
    and prints them to PagerDuty notes on the incident.
"""
import collections
import difflib
import json
import urllib2
import base64
import logging
from datetime import datetime
import boto3
import requests

print 'Loading function ' + datetime.now().time().isoformat()
logger = logging.getLogger()
logger.setLevel(logging.INFO)

username = 'vgpierce@spscommerce.com'
password = 'PASSWORD'

def lambda_handler(event, context):
    """
        parse out json message looking for ID, SERVICE, and NAME. Send these to retrieve_SOP
        method.
    """

    print event
    sns_message = json.loads(event['Records'][0]['Sns']['Message'])
    ID = sns_message["id"]
    SERVICE = sns_message["service"]
    NAME = sns_message["name"]
    STATUS = sns_message["status"]
    if STATUS == "triggered":
        print ID
        print NAME
        print SERVICE
        print STATUS
        message = retrieve_SOP(NAME)
        print message
        print pd_comment(ID, message)


def retrieve_SOP(name):
    """
        This method queries the Confluence Wiki API to find relevant SOPs and return links to the
        SOPs.
    """
    #get_creds()
    url = "https://atlassian.spscommerce.com/wiki/rest/api/content/search?limit=1500&start=0&queryString=SOP-&cql=siteSearch+~+%22SOP-%22+and+type+%3D+%22page%22"
    print datetime.now().time().isoformat()
    result = requests.get(url, auth=(username,password)).text
    print datetime.now().time().isoformat()
    jsonResult = json.loads(result)
    data_hash = jsonResult["results"]
    linkList = {1 :'Possible links:'}

    for SOP in data_hash:
        a = name.lower()
        b = SOP['title'].lower()
        if difflib.SequenceMatcher(None, a, b).ratio()>=.5:
            targetUrl = "https://atlassian.spscommerce.com/wiki{self}"\
            .format(self=SOP['_links']['webui'])
            linkList[difflib.SequenceMatcher(None, a, b).ratio()] = targetUrl
    linkList = collections.OrderedDict(sorted(linkList.items()))
    linkList = collections.OrderedDict(reversed(linkList.items()))

    result = ""
    for link in linkList:
        result+= linkList[link]+"\n"

    return result

def pd_comment(incident_id, message):
    """
        Invokes pd-commenter lambda with the possible related links.
    """
    aws_lambda = boto3.client('lambda')
    message_input = {
        'incidentID': incident_id,
        'message': message,
        }
    response = aws_lambda.invoke(
        FunctionName='dev-oaf-pd-commenter-CommenterFunction-1XX6T6Y7RR4G',
        InvocationType='RequestResponse',
        LogType='None',
        Payload=json.dumps(message_input)
    )
    return json.loads(response['Payload'].read())

if __name__ == '__main__':
    context = {}
    with open('event_message1.json', 'r') as f:
        event = f.read()
    print event
    lambda_handler(event, context)
