import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { FullMedicalAppointment } from '../models/full-medical-appointment.model';
import { MedicalAppointmentDataAllowedToUpdateDto } from '../models/MedicalAppointmentDataAllowedToUpdateDto.model';
import { MedicalAppointmentStateEnum } from '../models/medical-appointment.model';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class MedicalAppointmentService {
  urlBase: string = `${environment.apiUrl}/appointment`;

  constructor(private httpClient: HttpClient) { }

  public getMedicalAppointmentsByProfessional(professionalDni: string): Observable<FullMedicalAppointment[]> {
    return this.httpClient.get<FullMedicalAppointment[]>(`${this.urlBase}/get/professional/${professionalDni}`);
  }

  public cancelAppointment(appointmentId: number): Observable<any> {
    return this.httpClient.patch<any>(`${this.urlBase}/cancelAppointment/${appointmentId}`, {});
  }

  rescheduleAppointment(medicalAppointmentId: number, updateData: MedicalAppointmentDataAllowedToUpdateDto): Observable<any> {
    const requestBody = {
      appointmentDate: updateData.date,
      appointmentTime: updateData.time
    };
    return this.httpClient.put<any>(
      `${this.urlBase}/rescheduleMedicalAppointment/${medicalAppointmentId}`,
      requestBody
    );
  }

  public getMedicalAppointmentsByState(medicalAppState: MedicalAppointmentStateEnum): Observable<FullMedicalAppointment[]> {
    return this.httpClient.get<FullMedicalAppointment[]>(`${this.urlBase}/getByState/${medicalAppState}`);
  }

  public getAppointmentByFilters(params: HttpParams): Observable<FullMedicalAppointment[]> {
    return this.httpClient.get<FullMedicalAppointment[]>(`${this.urlBase}/findAppointmentByFilters`, { params });
  }

  public getAllMedicalAppointmentByDate(date:string): Observable<FullMedicalAppointment[]>{
    return this.httpClient.get<FullMedicalAppointment[]>(`${this.urlBase}/getByDate/${date}`);
  }

  public getAllMedicalAppointments(): Observable<FullMedicalAppointment[]>{
    return this.httpClient.get<FullMedicalAppointment[]>(`${this.urlBase}/get`);
  }



}
