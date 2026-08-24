import urllib.request
import json
import time
import subprocess
import threading

def send_payload(name, data, expected_status=None, is_json=True):
    print(f"\n--- Running Crash Test: {name} ---")
    try:
        req = urllib.request.Request("http://localhost:8080/api/evidence/sync", method="POST")
        if is_json:
            req.add_header('Content-Type', 'application/json')
            payload_bytes = json.dumps(data).encode('utf-8') if not isinstance(data, bytes) else data
        else:
            payload_bytes = data
            
        response = urllib.request.urlopen(req, data=payload_bytes, timeout=2)
        print(f"Result: SUCCESS (HTTP {response.status})")
    except urllib.error.HTTPError as e:
        print(f"Result: HANDLED ERROR (HTTP {e.code})")
    except Exception as e:
        print(f"Result: SERVER DROPPED CONNECTION OR TIMEOUT! ({e})")

# 1. Start Server
print("Starting Office Kit Backend Server for Crash Testing...")
server_process = subprocess.Popen(["python", "office_kit_backend.py"], stdout=subprocess.PIPE, stderr=subprocess.PIPE)
time.sleep(2) # Wait for server to bind

try:
    # Test 1: Malformed JSON
    send_payload("Malformed JSON", b"{ invalid_json: 'test' ", is_json=False)

    # Test 2: Missing Fields (Empty JSON)
    send_payload("Empty JSON Payload", {})

    # Test 3: Incorrect Data Types (evidenceChain as string instead of list)
    send_payload("Incorrect Data Types", {
        "entityId": "Panel A17",
        "evidenceChain": "This should be a list, but it's a string!",
        "changes": None
    })

    # Test 4: Extremely Large Payload (10MB)
    print("\n--- Running Crash Test: 10MB Massive Payload ---")
    massive_payload = {
        "entityId": "Panel A17",
        "evidenceChain": [{"date": "Aug 18", "condition": "Normal"} for _ in range(100000)],
        "changes": []
    }
    send_payload("10MB Payload", massive_payload)

    # Test 5: Rapid Fire (DDOS simulation)
    print("\n--- Running Crash Test: Rapid Fire (100 concurrent requests) ---")
    threads = []
    valid_payload = {"entityId": "Spam", "evidenceChain": [], "changes": []}
    
    def spam():
        try:
            req = urllib.request.Request("http://localhost:8080/api/evidence/sync", method="POST")
            req.add_header('Content-Type', 'application/json')
            urllib.request.urlopen(req, data=json.dumps(valid_payload).encode('utf-8'), timeout=5)
        except:
            pass

    for _ in range(100):
        t = threading.Thread(target=spam)
        threads.append(t)
        t.start()
        
    for t in threads:
        t.join()
    print("Rapid Fire complete.")

    # Final check: Is server still alive?
    print("\n--- Final Health Check ---")
    send_payload("Health Check after Chaos", {"entityId": "Alive", "evidenceChain": [], "changes": []})

finally:
    server_process.terminate()
    print("\nCrash tests completed. Server terminated.")
