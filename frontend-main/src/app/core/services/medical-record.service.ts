import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MedicalRecord } from '../models/medical-record.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MedicalRecordService {
 private baseUrl = 'http://localhost:8080/api/medical-records';

  constructor(private http: HttpClient) { }

  public updateMedicalRecord(patientDni: string, medicalRercord: MedicalRecord): Observable<any> {
    return this.http.put(`${this.baseUrl}/updateMedicalRecord/${patientDni}`, medicalRercord);
  }

}
