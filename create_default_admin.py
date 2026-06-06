import urllib.request
import json
import urllib.error

url = "http://localhost:8080/api/auth/register"
payload = {
    "dni": "12345678",
    "name": "Admin",
    "lastname": "Wolf",
    "address": "Av Mitre 1234",
    "email": "admin@wolfdentistry.com",
    "phoneNumber": "1145678901",
    "username": "admin",
    "password": "adminPassword123",
    "role": "ADMIN",
    "accountState": "ACTIVE",
    "newAccount": False
}

req = urllib.request.Request(url, method="POST")
req.add_header("Content-Type", "application/json")
data = json.dumps(payload).encode("utf-8")

try:
    with urllib.request.urlopen(req, data=data) as response:
        res_data = response.read().decode("utf-8")
        print("Respuesta de registro:", res_data)
except urllib.error.HTTPError as e:
    print("HTTP Error code:", e.code)
    try:
        err_body = e.read().decode("utf-8")
        print("Detalle del error:", err_body)
    except Exception as ex:
        print("No se pudo leer el cuerpo del error:", ex)
except Exception as e:
    print("Otro error:", e)
