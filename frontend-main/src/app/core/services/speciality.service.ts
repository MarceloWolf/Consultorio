import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Speciality } from '../models/speciality.model';
import { Observable } from 'rxjs';
import { SpecialityDto } from '../models/specialityDto.model';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class SpecialityService {
  urlBase: string = `${environment.apiUrl}/speciality`;

  constructor(private httpClient: HttpClient) {}

  public getSpecialities(): Observable<Speciality[]> {
    return this.httpClient.get<Speciality[]>(`${this.urlBase}/get`);
  }

  public addSpeciality(speciality:string): Observable<Speciality> {
    const specialityDto:SpecialityDto = {
      name:speciality
    };
    
    return this.httpClient.post<Speciality>(`${this.urlBase}/add`,specialityDto,{
      headers: new HttpHeaders({
        'Content-Type': 'application/json'
      })
    });
  }
  
  public deleteSpeciality(speciality:string): Observable<any> {
    return this.httpClient.delete<any>(`${this.urlBase}/deleteSpeciality/${speciality}`);
  }

  public getSpecialitiesByProfessional(professionalDni:string):Observable<Speciality[]>{
    return this.httpClient.get<Speciality[]>(`${this.urlBase}/get/professional/${professionalDni}`);
  }

}
