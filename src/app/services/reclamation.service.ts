import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReclamationService {
  private apiUrl = 'http://localhost:8082/api/reclamations'; // Replace with your backend API

  constructor(private http: HttpClient) { }
  
  addReclamation(reclamation: any): Observable<string> {
    return this.http.post<string>(`${this.apiUrl}/addReclamation`, reclamation);
  }
  addResponse(responseData: any): Observable<any> {
    const url = `${this.apiUrl}/addResponse`;
    return this.http.post(url, responseData);
  }
  
  getReclamations(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/all`);
  }
  updateReclamation(id: string, newTitle: string, newDescription: string, newDate: string): Observable<string> {
    const params = new HttpParams()
      .set('newTitle', newTitle)
      .set('newDescription', newDescription)
      .set('newDate', newDate);

    return this.http.put<string>(`${this.apiUrl}/update/${id}`, params);
  }
  deleteReclamation(id: string): Observable<string> {
    return this.http.delete<string>(`${this.apiUrl}/delete/${id}`);
  }
 
  getResponsesForReclamation(reclamationId: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${reclamationId}/responses`);
  }
  deleteResponse(id: string): Observable<any> {
    return this.http.delete<any>(`${this.apiUrl}/responses/delete/${id}`);
  }
}
