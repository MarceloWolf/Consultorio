import urllib.request
import json
import urllib.error

# 1. Login to get token
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

# 2. Test GET /api/speciality/get
spec_url = "http://localhost:8080/api/speciality/get"
req_spec = urllib.request.Request(spec_url, method="GET")
req_spec.add_header("Authorization", f"Bearer {token}")

try:
    with urllib.request.urlopen(req_spec) as response:
        print("GET /api/speciality/get SUCCESS. Status:", response.status)
except urllib.error.HTTPError as e:
    print("GET /api/speciality/get FAILED with status:", e.code)
    try:
        print("Error details:", e.read().decode("utf-8"))
    except:
        pass

# 3. Test GET /api/appointment/get/professional/22513480
app_url = "http://localhost:8080/api/appointment/get/professional/22513480"
req_app = urllib.request.Request(app_url, method="GET")
req_app.add_header("Authorization", f"Bearer {token}")

try:
    with urllib.request.urlopen(req_app) as response:
        print("GET /api/appointment/get/professional/22513480 SUCCESS. Status:", response.status)
except urllib.error.HTTPError as e:
    print("GET /api/appointment/get/professional/22513480 FAILED with status:", e.code)
    try:
        print("Error details:", e.read().decode("utf-8"))
    except:
        pass
