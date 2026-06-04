import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { jwtDecode } from 'jwt-decode';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  urlBase: string = ('http://localhost:8080/api/auth');

  constructor(private _httpClient: HttpClient) { }

  public login(username: string, password: string): Observable<any> {
    const payload = {
      username: username,
      password: password,
    };
    return this._httpClient.post<{ token: string }>(`${this.urlBase}/authenticate`, payload).pipe(tap(response => {
      localStorage.setItem('token', response.token);
    }))
  }

  public logOut() {
    localStorage.removeItem('token');
  }

  public getToken(): string | null {
    return localStorage.getItem('token');
  }

  public isAuthenticated(): boolean {
    return !!this.getToken();
  }

  public getInfoToken():any
  {
    const token = this.getToken();
    if(!token) return null;

    try {
       return jwtDecode(token);
    } catch (error) {
      console.error('Error al decodificar el token:', error);
      return null;
    }
  }

}
