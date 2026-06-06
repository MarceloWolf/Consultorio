import urllib.request
import json

url = "http://localhost:8080/api/auth/authenticate"
payload = {
    "username": "admin",
    "password": "adminPassword123"
}

req = urllib.request.Request(url, method="POST")
req.add_header("Content-Type", "application/json")
data = json.dumps(payload).encode("utf-8")

try:
    with urllib.request.urlopen(req, data=data) as response:
        res_data = response.read().decode("utf-8")
        print("Login Exitoso:", res_data)
except Exception as e:
    print("Error al loguearse con admin:", e)
