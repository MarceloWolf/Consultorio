import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MedicalRecord } from '../models/medical-record.model';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class MedicalRecordService {
 private baseUrl = `${environment.apiUrl}/medical-records`;

  constructor(private http: HttpClient) { }

  public updateMedicalRecord(patientDni: string, medicalRercord: MedicalRecord): Observable<any> {
    return this.http.put(`${this.baseUrl}/updateMedicalRecord/${patientDni}`, medicalRercord);
  }

}
