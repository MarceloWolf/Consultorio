import urllib.request
import json
import urllib.error

# 1. Login to get token
login_url = "http://localhost:8080/api/auth/authenticate"
login_payload = {"username": "mvoigt", "password": "mvoigt123"}

req = urllib.request.Request(login_url, method="POST")
req.add_header("Content-Type", "application/json")
data = json.dumps(login_payload).encode("utf-8")

try:
    with urllib.request.urlopen(req, data=data) as response:
        res_data = json.loads(response.read().decode("utf-8"))
        token = res_data.get("token")
        print("Login SUCCESS for mvoigt")
except Exception as e:
    print("Login FAILED:", e)
    exit(1)

# 2. Test GET /api/users/PROFESSIONAL
url1 = "http://localhost:8080/api/users/PROFESSIONAL"
req1 = urllib.request.Request(url1, method="GET")
req1.add_header("Authorization", f"Bearer {token}")

try:
    with urllib.request.urlopen(req1) as response:
        print("GET /api/users/PROFESSIONAL SUCCESS. Status:", response.status)
except urllib.error.HTTPError as e:
    print("GET /api/users/PROFESSIONAL FAILED with status:", e.code)
    try:
        print("Error details:", e.read().decode("utf-8"))
    except:
        pass

# 3. Test GET /api/secretary/findAllByState/true
url2 = "http://localhost:8080/api/secretary/findAllByState/true"
req2 = urllib.request.Request(url2, method="GET")
req2.add_header("Authorization", f"Bearer {token}")

try:
    with urllib.request.urlopen(req2) as response:
        print("GET /api/secretary/findAllByState/true SUCCESS. Status:", response.status)
except urllib.error.HTTPError as e:
    print("GET /api/secretary/findAllByState/true FAILED with status:", e.code)
    try:
        print("Error details:", e.read().decode("utf-8"))
    except:
        pass
