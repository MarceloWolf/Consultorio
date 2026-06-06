import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface MedicalImage {
  id?: number;
  fileName: string;
  fileType: string;
  comments: string;
  imageData: string; // Base64
  uploadDate?: string;
}

@Injectable({
  providedIn: 'root'
})
export class MedicalImageService {
  private baseUrl = `${environment.apiUrl}/medical-images`;

  constructor(private http: HttpClient) { }

  public getMedicalImages(patientDni: string): Observable<MedicalImage[]> {
    return this.http.get<MedicalImage[]>(`${this.baseUrl}/${patientDni}`);
  }

  public getMedicalImagesByType(patientDni: string, fileType: string): Observable<MedicalImage[]> {
    return this.http.get<MedicalImage[]>(`${this.baseUrl}/${patientDni}/type/${fileType}`);
  }

  public uploadMedicalImage(patientDni: string, image: { fileName: string, fileType: string, comments: string, imageData: string }): Observable<MedicalImage> {
    return this.http.post<MedicalImage>(`${this.baseUrl}/${patientDni}`, image);
  }

  public deleteMedicalImage(imageId: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${imageId}`);
  }
}
