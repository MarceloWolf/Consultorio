import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { last, Observable, of } from 'rxjs';
import { Patient } from '../models/patient.model';
import { MedicalAppointment } from '../models/medical-appointment.model';
import { MedicalRecord } from '../models/medical-record.model';
import { FullMedicalAppointment } from '../models/full-medical-appointment.model';
import { Professional } from '../models/professional.model';

@Injectable({
  providedIn: 'root'
})
export class PatientService {
  private baseUrl = 'http://localhost:8080/api/secretary';

  constructor(private http: HttpClient) { }

  // Pacientes
  getPatients(): Observable<Patient[]> {
    return this.http.get<Patient[]>(`${this.baseUrl}/get`);
  }

  getPatientByDni(dni: string): Observable<Patient> {
    return this.http.get<Patient>(`${this.baseUrl}/findPatientByDNI/${dni}`);
  }

  getPatientByLastname(lastname: string): Observable<Patient>{
    return this.http.get<Patient>(`${this.baseUrl}/findPatientByLastname/${lastname}`);
  }

  createPatient(patient: Patient): Observable<any> {
    return this.http.post(`${this.baseUrl}/addPatient`, patient);
  }

  updatePatient(dni: string, patient: Patient): Observable<any> {
    return this.http.put(`${this.baseUrl}/updatePatient/${dni}`, patient);
  }

  updatePatientState(dni: string, state: boolean): Observable<any> {
    return this.http.patch(`${this.baseUrl}/updatePatientState/${dni}/${state}`, {});
  }
  getPatientsByState(accountState: boolean): Observable<Patient[]> {
    return this.http.get<Patient[]>(`${this.baseUrl}/findAllByState/${accountState}`);
  }

  // Historial Médico
  getMedicalRecord(dni: string): Observable<MedicalRecord> {
    return this.http.get<MedicalRecord>(`${this.baseUrl}/findMedicalRecord/${dni}`);
  }

  // Citas Médicas
  createMedicalAppointment(appointment: MedicalAppointment): Observable<any> {
    return this.http.post(`${this.baseUrl}/addMedicalAppointment`, appointment);
  }

  getMedicalAppointmentsByPatient(dni: string): Observable<FullMedicalAppointment[]> {
    return this.http.get<FullMedicalAppointment[]>(`${this.baseUrl}/findMedicalAppointmentByPatient/${dni}`);
  }
  getMedicalAppointmentsByPatientAndSpeciality(specialityName: string, patientDni: string): Observable<FullMedicalAppointment[]> {
    return this.http.get<FullMedicalAppointment[]>(`${this.baseUrl}/findMedicalAppointmentByPatient/${patientDni}/AndSpeciality/${specialityName}`);
  }

  updateAppointmentState(id: number, state: string): Observable<any> {
    return this.http.patch(`${this.baseUrl}/updateMedicalAppointmentState/${id}/${state}`, {});
  }

  getProfessionalsBySpeciality(specialityName: string): Observable<Professional[]>{
    return this.http.get<Professional[]>(`${this.baseUrl}/findProfessional/${specialityName}`);
  }

}