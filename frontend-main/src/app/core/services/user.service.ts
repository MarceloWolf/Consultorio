import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../models/user.model';
import { Professional } from '../models/professional.model';
import { Secretary } from '../models/secretary.model';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  urlBase: string = `${environment.apiUrl}/users`;

  constructor(private httpClient: HttpClient) {}

  public getUsers(): Observable<User[]> {
    return this.httpClient.get<User[]>(this.urlBase);
  }

  public getUsersByRole(role: string): Observable<User[]> {
    return this.httpClient.get<User[]>(`${this.urlBase}/${role}`);
  }

  public getUserByDni(dni: string): Observable<User> {
    return this.httpClient.get<User>(`${this.urlBase}/user/findByDni/${dni}`);
  }

  public getUserByUsername(username: string): Observable<User> {
    return this.httpClient.get<User>(`${this.urlBase}/user/findByUsername/${username}`);
  }

  public getProfessionalsByState(state: string): Observable<User[]> {
    return this.httpClient.get<User[]>(`${this.urlBase}/professional/${state}`);
  }

  public getSecretaryByState(state: string): Observable<User[]> {
    return this.httpClient.get<User[]>(`${this.urlBase}/secretary/${state}`);
  }

  public deleteProfessional(dni: string): Observable<User> {
    return this.httpClient.patch<User>(
      `${this.urlBase}/professional/${dni}/delete`,
      {}
    );
  }

  public deleteSecretary(dni: string): Observable<User> {
    return this.httpClient.patch<User>(
      `${this.urlBase}/secretary/${dni}/delete`,
      {}
    );
  }

  public reactivateUser(dni: string): Observable<User> {
    return this.httpClient.patch<User>(
      `${this.urlBase}/reactivateUser/${dni}`,
      {}
    );
  }

  public updatePassword(
    username: String,
    newPassword: string
  ): Observable<User> {
    return this.httpClient.patch<User>(
      `${this.urlBase}/updateUserPassword/${username}/${newPassword}`,{}
    );
  }

  public updateSecretary(dni: String, user: User): Observable<User> {
    return this.httpClient.put<User>(
      `${this.urlBase}/updateSecretary/${dni}`,
      user
    );
  }

  public updateProfessional(dni: String, user: User): Observable<User> {
    return this.httpClient.put<User>(
      `${this.urlBase}/updateProfessional/${dni}`,
      user
    );
  }

  public createProfessional(user: Professional): Observable<User> {
    const payload = {
      professional: user,
      specialityNames: user.specialityList,
    };
    console.log(payload.professional);
    console.log(payload.specialityNames);
    return this.httpClient.post<User>(
      `${this.urlBase}/addProfessionalWithSpecialities`,
      payload
    );
  }

  public createSecretary(user: Secretary): Observable<User> {
    return this.httpClient.post<User>(
      `${this.urlBase}/addSecretary`,
      user
    );
  }
}
