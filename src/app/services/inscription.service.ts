import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { AuthService } from './auth.service'; // Assuming AuthService is implemented

@Injectable({
  providedIn: 'root',
})
export class InscriptionService {
  private apiUrl = 'http://localhost:8082'; // Updated base URL

  constructor(private http: HttpClient, private authService: AuthService) {}

  // Get the Authorization header
  private getAuthHeaders(): HttpHeaders {
    const token = this.authService.getToken(); // Get the token from AuthService

    if (!token) {
      console.error('No token found');
      // Handle the case where the token is missing, maybe navigate to login
    }

    console.log('Token in header:', token); // Debugging to ensure the token is valid

    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }

  // Get all inscriptions
  getAllInscriptions(): Observable<any> {
    const url = `${this.apiUrl}/inscriptions`;
    const headers = this.getAuthHeaders();  // Include the token in the headers

    return this.http.get<any>(url, { headers }).pipe(
      map((response: any) => {
        return response.map((item: any) => ({
          email: item.hasEmail,
          formationId: item.hasFormation.split('#')[1], // Extract the formation ID from the URL
          inscriptionType: item.inscriptionType,
        }));
      })
    );
  }

  // Delete an inscription
  deleteInscription(email: string, formationId: string): Observable<any> {
    const url = `${this.apiUrl}/inscriptions/${email}/${formationId}`;
    const headers = this.getAuthHeaders();  // Include the token in the headers

    return this.http.delete(url, { headers }).pipe(
      map((response: any) => response) // Handle the response if needed
    );
  }

  // Add a new inscription
  addInscription(inscription: any): Observable<any> {
    const url = `${this.apiUrl}/inscriptions`;
    const headers = this.getAuthHeaders();  // Include the token in the headers

    return this.http.post(url, inscription, { headers }).pipe(
      map((response: any) => response) // Handle the response if needed
    );
  }
}

interface Inscription {
  email: string;
  formationId: string;
  inscriptionType: string;
}
