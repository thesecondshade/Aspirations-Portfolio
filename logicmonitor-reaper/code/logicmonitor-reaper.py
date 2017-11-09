"""
	This script is ran periodically to prune hosts from LogicMonitor service. This 
	is especially important as we migrate to AWS and autoscaling becomes standard.
"""

# stdlib imports
import json
import logging
import optparse
import os
import re
import sys

# 3rd party imports
from credstash import getSecret
import requests


logging.basicConfig()
logging.getLogger('boto3').setLevel(logging.CRITICAL)
logging.getLogger('botocore').setLevel(logging.CRITICAL)
logger = logging.getLogger("mylogger")
logger.setLevel("DEBUG")

user = None
key = None

def get_creds():
	"""Reads a user and key out of credstash to auth into the LM API."""

	global user
	global key
	if user or key == None:
		user = getSecret('sre.oaf.utility.oaf-logicmonitor-reaper.id',
				region='us-east-1',
				table='{}-credstash'.format(os.environ['environ']))
		key = getSecret('sre.oaf.utility.oaf-logicmonitor-reaper.key',
				region='us-east-1',
				table='{}-credstash'.format(os.environ['environ']))
	
def issue_request(url):
	"""Requests a url and returns the JSON content."""

	buffer =requests.get(url)
	return buffer.json()

def Main():
	"""Uses previously described methods to auth into and receive JSON data from 
	the LM API. Parses through servers testing each on their IPS and their 
	status. Then if the servers have been dead for more than a day, they are 
	deleted."""
    
	get_creds()
	company = 'spscommerce'
	uri = 'https://{0}.logicmonitor.com/santaba/rpc/getHosts?c={0}&u={1}&p={2}&hostGroupId=1'.format(company, user, key)
	jsonResponse = issue_request(uri)
	data_hash = jsonResponse["data"]
	p = re.compile("10\.(100|64|96|66|98|128|129|160|161|192|193|224|225|228|229|255)\.")

	for host in data_hash['hosts']:
		if host['status'] =='dead' and p.match(host['properties']['system.ips']):
			hid = host['id']
			name = host['displayedAs']
			
			uri2 = 'https://{0}.logicmonitor.com/santaba/rpc/getData?c={0}&u={1}&p={2}&host={3}&dataSource=HostStatus&dataPoint0=IdleInterval'.format(company, user, key, name)
			jsonResponse2=issue_request(uri2)
			data_hash2 = jsonResponse2["data"]
			time = data_hash2['values']['HostStatus'][0][2]
			if time >86400:
				logger.info("Not Reporting for > 1 day")
				logger.info("Host ID: "+ str(hid))
				rm = 'https://{0}.logicmonitor.com/santaba/rpc/deleteHost?c={0}&u={1}&p={2}&hostId={3}&deleteFromSystem=true'.format(company,user,key,str(hid))
				logger.info("Running: "+rm)
				requests.get(rm)


def lambda_handler(event, context):
	logger.info(json.dumps(event, indent=4))
	Main()

if __name__ == '__main__':
	os.environ['environ'] = 'dev'
	event = {}
	context = {}
	lambda_handler(event, context)
