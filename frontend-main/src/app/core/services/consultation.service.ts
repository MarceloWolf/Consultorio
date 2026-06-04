import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Consultation } from '../models/consultation.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ConsultationService {

   urlBase: string = 'http://localhost:8080/api/consultations';
 
   constructor(private httpClient: HttpClient) {}


  public getConsultationByMedicalRecordId(medicalRecordId:number):Observable<Consultation[]>{
    return this.httpClient.get<Consultation[]>(`${this.urlBase}/medical-record/${medicalRecordId}`);
  }

  public getConsultationBySpeciality(specialityName:string):Observable<Consultation[]>{
    return this.httpClient.get<Consultation[]>(`${this.urlBase}/findAllBySpeciality/${specialityName}`);
  }


}
