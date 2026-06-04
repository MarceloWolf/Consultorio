import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Consultation } from '../models/consultation.model';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ConsultationService {

   urlBase: string = `${environment.apiUrl}/consultations`;
 
   constructor(private httpClient: HttpClient) {}


  public getConsultationByMedicalRecordId(medicalRecordId:number):Observable<Consultation[]>{
    return this.httpClient.get<Consultation[]>(`${this.urlBase}/medical-record/${medicalRecordId}`);
  }

  public getConsultationBySpeciality(specialityName:string):Observable<Consultation[]>{
    return this.httpClient.get<Consultation[]>(`${this.urlBase}/findAllBySpeciality/${specialityName}`);
  }


}
