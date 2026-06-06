import mysql.connector

try:
    conn = mysql.connector.connect(
        host="localhost",
        user="root",
        password="",
        database="consultorio"
    )
    cursor = conn.cursor()
    cursor.execute("SHOW TABLES")
    print("Tables:", cursor.fetchall())
    cursor.execute("SELECT id, dni, username, name, lastname, role, password FROM user")
    print("Users in 'user' table:")
    for row in cursor.fetchall():
        print(row)
    cursor.execute("SELECT id, username, password, admin_id FROM admin_user")
    print("Admin Users in 'admin_user' table:")
    for row in cursor.fetchall():
        print(row)
except Exception as e:
    print("Error:", e)
