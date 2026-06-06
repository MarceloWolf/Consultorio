import urllib.request
import json
import urllib.error

# 1. Login as pwolf
login_url = "http://localhost:8080/api/auth/authenticate"
login_payload = {"username": "pwolf", "password": "pwolf123"}

req = urllib.request.Request(login_url, method="POST")
req.add_header("Content-Type", "application/json")
data = json.dumps(login_payload).encode("utf-8")

try:
    with urllib.request.urlopen(req, data=data) as response:
        res_data = json.loads(response.read().decode("utf-8"))
        token = res_data.get("token")
        print("Login SUCCESS for pwolf")
except Exception as e:
    print("Login FAILED:", e)
    exit(1)

# 2. Test GET /api/secretary/findPatientByDNI/45678901 (or another DNI)
url = "http://localhost:8080/api/secretary/findPatientByDNI/45678901"
req_get = urllib.request.Request(url, method="GET")
req_get.add_header("Authorization", f"Bearer {token}")

try:
    with urllib.request.urlopen(req_get) as response:
        print("GET /api/secretary/findPatientByDNI/45678901 SUCCESS. Status:", response.status)
except urllib.error.HTTPError as e:
    print("GET /api/secretary/findPatientByDNI/45678901 FAILED with status:", e.code)
    try:
        print("Error details:", e.read().decode("utf-8"))
    except:
        pass
