import urllib.request
import json
import time
import subprocess
import os

print("Starting Office Kit Backend Server...")
server_process = subprocess.Popen(["python", "office_kit_backend.py"])
time.sleep(2) # Wait for server to start

try:
    payload = {
        "entityId": "Panel A17",
        "evidenceChain": [
            {"date": "Aug 18", "condition": "Normal"},
            {"date": "Aug 21", "condition": "Minor corrosion"},
            {"date": "Aug 24", "condition": "Visible corrosion"}
        ],
        "changes": [
            {
                "attribute": "Condition",
                "previous": "Normal",
                "current": "Degraded",
                "confidence": 0.91
            }
        ]
    }
    
    req = urllib.request.Request("http://localhost:8080/api/evidence/sync", method="POST")
    req.add_header('Content-Type', 'application/json')
    
    print("\nSending Payload from Simulated Android Phone:")
    print(json.dumps(payload, indent=2))
    print("\n--- Server Response ---")
    
    response = urllib.request.urlopen(req, data=json.dumps(payload).encode('utf-8'))
    print(response.read().decode('utf-8'))
    print("-----------------------\n")
    print("Test Complete. Office Kit Sync Successful.")
    
finally:
    server_process.terminate()
