import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service'; // Import the AuthService to access the token

@Injectable({
  providedIn: 'root'
})
export class FormationService {
  private apiUrl = 'http://localhost:8082/formations';

  constructor(private http: HttpClient, private authService: AuthService) {}

  // Helper method to get the authorization headers
  private getAuthHeaders(): HttpHeaders {
    const token = this.authService.getToken();
    return new HttpHeaders({
      'Authorization': `Bearer ${token}` // Assuming token is in Bearer format
    });
  }

  // Get all formations with token in headers
  getAllFormations(): Observable<any> {
    return this.http.get(this.apiUrl, { headers: this.getAuthHeaders() });
  }

  // Add a new formation with token in headers
  addFormation(formation: any): Observable<any> {
    return this.http.post(this.apiUrl, formation, { headers: this.getAuthHeaders() });
  }

  // Update an existing formation with token in headers
  updateFormation(id: string, formation: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}`, formation, { headers: this.getAuthHeaders() });
  }

  // Delete a formation with token in headers
  deleteFormation(id: string): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`, { headers: this.getAuthHeaders() });
  }

  // Get formation by ID with token in headers
  getFormation(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${id}`, { headers: this.getAuthHeaders() });
  }
}
