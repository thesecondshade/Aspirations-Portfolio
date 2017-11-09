import os
import logging
import requests
import json
from credstash import getSecret

def create_incident_note(event, context):
    """
        This script uses the PagerDuty API to write notes to specific incidents.
    """
    EMAIL = getSecret('sre.oaf.utility.pd-commenter.username',
                          region='us-east-1',
                          table='{}-credstash'.format(os.environ['environ']))
    API_KEY = getSecret('sre.oaf.utility.pd-commenter.password',
                          region='us-east-1',
                          table='{}-credstash'.format(os.environ['environ']))
    INCIDENT_ID = event['incidentID']
    CONTENT = event['message']
    url = 'https://api.pagerduty.com/incidents/{id}/notes'.format(id=INCIDENT_ID)
    
    headers = {
        'Accept': 'application/vnd.pagerduty+json;version=2',
        'Authorization': 'Token token={token}'.format(token=API_KEY),
        'Content-type': 'application/json',
        'From': EMAIL
    }
    payload = {
        'note': {
            'content': CONTENT
        }
    }
    r = requests.post(url, headers=headers, data=json.dumps(payload))
    if r.status_code ==201:
        print("Note added successfully.")
    return "Note: "+CONTENT+" added successfully."

if __name__ == '__main__':
    import sys
    context = {}
    os.environ['environ'] = 'dev'
    event = {
        "incidentID": 535078,
        "message": "testing"
    }
    create_incident_note(event, context)

