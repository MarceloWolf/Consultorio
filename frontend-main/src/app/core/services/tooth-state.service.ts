import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface ToothState {
  id?: number;
  toothNumber: number;
  state: string;
  notes: string;
}

@Injectable({
  providedIn: 'root'
})
export class ToothStateService {
  private baseUrl = `${environment.apiUrl}/tooth-states`;

  constructor(private http: HttpClient) { }

  public getToothStates(patientDni: string): Observable<ToothState[]> {
    return this.http.get<ToothState[]>(`${this.baseUrl}/${patientDni}`);
  }

  public saveOrUpdateToothState(patientDni: string, toothState: { toothNumber: number, state: string, notes: string }): Observable<ToothState> {
    return this.http.post<ToothState>(`${this.baseUrl}/${patientDni}`, toothState);
  }
}
