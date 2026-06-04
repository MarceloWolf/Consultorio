# CapacitacionBackend
# By Lucas Diez and Santiago Rubini

Este repositorio contiene información y plantillas para la gestión de un sistema medico. A continuacion, se encuentra un script SQL y ejemplos utiles para la configuración de especialidades, personal administrativo, profesionales, pacientes, turnos, registros médicos y consultas.

---

### Script para generar la Base de Datos

```sql
CREATE DATABASE IF NOT EXISTS consultorio_medico;
```

### Script para tener base de Especialidades
```sql
INSERT INTO speciality (name) 
VALUES ('Clinica medica'), ('Dermatologia'), ('Pediatria'), ('Cardiologia');
```
---
# Como ingresar el Admin
Para ingresar el admin,deben ingresar al swagger(http://localhost:8080/swagger-ui/index.html#/auth-controller/authenticate) y desde alli deben ir al controller de Auth y registrar la plantilla de admin que tienen a continuacion.
Luego les enviaran el Token del admin como respuesta y deberan agregarlo en el Authorize que les aparece arriba a la derecha del secretary controller  del swagger para poder
realizar los metodos de todos los controllers.
Luego el admin podra registrar el professional y la secretary y les devolvera el token de cada uno de ellos.
Para poder verificar el funcionamiento de alguno de ellos, deberan apretar devuelta Authorize para 
hacer el logout del admin y podran ingresar el token de secretary o profesional, este loguout lo 
deberan hacer cada vez que quieran probar el funcionamiento de los diferentes usuarios.
---
# Aqui se encuentran los recursos basicos para probar los endpoints del sistema 
## Plantillas

Admin
```
{
   "dni": "20456987",
   "name": "Pedro",
   "lastname": "Gonzales",
   "address": "14 nro1234",
   "email": "pedrogonzales@gmail.com",
   "phoneNumber": "22154332",
   "username": "admin",
   "password": "admin123"
}
```
Secretaria 

```
{
  "dni": "2143534",
  "name": "Agustin",
  "lastname": "Gonzales",
  "address": "2345",
  "email": "agolzales@test.com",
  "phoneNumber": "66524123",
  "username": "agonzales",
  "password": "agnzales123",
  "start": "08:00",
  "end": "13:00",
}
```
Profesional 
```
{
  "professional": {
    "dni": "12111333",
    "name": "lucas",
    "lastname": "diez",
    "address": "1234",
    "email": "ldiez@gmail.com",
    "phoneNumber": "2210954321",
    "username": "ldiez",
    "password": "ldiez123",
    "start": "10:00",
    "end": "15:00",
    "businessDays": [
      { "dayOfWeek": "MONDAY" },
      { "dayOfWeek": "WEDNESDAY" },
      { "dayOfWeek": "THURSDAY" }
    ]
  },
  "specialityNames": [
    { "name": "Cardiologia" }
  ]
}
```
Profesional con varias especialidades
```
{
  "professional": {
    "dni": "12111123",
    "name": "santiago",
    "lastname": "rubini",
    "address": "12345",
    "email": "srubini@gmail.com",
    "phoneNumber": "2210951234",
    "username": "srubini",
    "password": "srubini123",
    "start": "10:00",
    "end": "15:00",
    "businessDays": [
      { "dayOfWeek": "MONDAY" },
      { "dayOfWeek": "WEDNESDAY" },
      { "dayOfWeek": "THURSDAY" }
    ]
  },
  "specialityNames": [
     { "name": "Cardiologia" },
     { "name": "Dermatologia" } 
  ]
}
```
Plantilla Paciente
```
{
  "dni": "12345632",
  "name": "Julian",
  "lastname": "Pérez",
  "address": "Calle Falsa 1232",
  "email": "Julian.perez@example.com",
  "phoneNumber": "+3412346434",
  "birthdate":"2003-01-20"
}
```
Plantilla turno
```
{
  "patientDni": "12345632B",
  "professionalDni": "30500123",
  "specialityName": "Cardiologia",
  "date": "2025-01-18",
  "time": "10:30"
}
```
Plantilla editar turno
```
{
  "appointmentDate": "2025-01-20",
  "appointmentTime": "10:00"
}
```
Plantilla actualizacion historial medico
```
{
  "description": "Patient's general medical record",
  "height": 1.75,
  "weight": 70.5,
  "bloodGroup": "O+"
}
```
Plantilla consultation
```
{
  "specialityName": "Cardiologia"
  "reason": "Routine check-up",
  "diagnosis": "Mild hypertension",
  "treatment": "Prescribed a low-sodium diet and regular exercise"
}
```

