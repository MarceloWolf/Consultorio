import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Professional } from '../models/professional.model';
import { MedicalRecord } from '../models/medical-record.model';
import { Consultation } from '../models/consultation.model';
import { CreateConsultation } from '../models/createConsultation.model';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ProfessionalService {

  private baseUrl = `${environment.apiUrl}/professional`;
 
  constructor(private http: HttpClient) { }

  public getProfessionalByDni(dni:string):Observable<Professional>{
    return this.http.get<Professional>(`${this.baseUrl}/get/${dni}`);
  }
  public getMedicalRecordByPatientDni(dni:string):Observable<MedicalRecord[]>{
    return this.http.get<MedicalRecord[]>(`${this.baseUrl}/getMedicalRecord/${dni}`);
  }

  public addConsultation(professionalDni:string, patientDni:string, specialityName:string, consultation: CreateConsultation):Observable<any>{
    return this.http.post<any>(`${this.baseUrl}/consultation/medical-record/${professionalDni}/${patientDni}/${specialityName}`, consultation)
  }
}

