import urllib.request
import json

login_url = "http://localhost:8080/api/auth/authenticate"
login_payload = {"username": "mvoigt", "password": "mvoigt123"}

req = urllib.request.Request(login_url, method="POST")
req.add_header("Content-Type", "application/json")
data = json.dumps(login_payload).encode("utf-8")

try:
    with urllib.request.urlopen(req, data=data) as response:
        token = json.loads(response.read().decode("utf-8")).get("token")
except Exception as e:
    print("Login FAILED:", e)
    exit(1)

url = "http://localhost:8080/api/secretary/get"
req_get = urllib.request.Request(url, method="GET")
req_get.add_header("Authorization", f"Bearer {token}")

try:
    with urllib.request.urlopen(req_get) as response:
        patients = json.loads(response.read().decode("utf-8"))
        for p in patients:
            print(f"DNI: {p.get('dni')}, Name: {p.get('name')}, Lastname: {p.get('lastname')}")
except Exception as e:
    print("Request FAILED:", e)
