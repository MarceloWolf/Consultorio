import urllib.request
import json
import urllib.error

users = [
    {"username": "mwolf", "password": "mwolf123"},
    {"username": "pwolf", "password": "pwolf123"},
    {"username": "mvoigt", "password": "mvoigt123"}
]

url = "http://localhost:8080/api/auth/authenticate"

for u in users:
    req = urllib.request.Request(url, method="POST")
    req.add_header("Content-Type", "application/json")
    data = json.dumps(u).encode("utf-8")
    try:
        with urllib.request.urlopen(req, data=data) as response:
            res_data = json.loads(response.read().decode("utf-8"))
            print(f"Login SUCCESS for {u['username']}: Token length = {len(res_data.get('token', ''))}")
    except urllib.error.HTTPError as e:
        print(f"HTTP Error for {u['username']}: {e.code}")
        try:
            print("Error detail:", e.read().decode("utf-8"))
        except Exception:
            pass
    except Exception as e:
        print(f"Error for {u['username']}: {e}")
