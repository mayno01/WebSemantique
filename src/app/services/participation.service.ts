import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ParticipationService {
  private apiUrl = 'http://localhost:8082/api/participations'; 

  constructor(private http: HttpClient) {}

  // Fetch the token from cookies
  private getToken(): string | null {
    const token = document.cookie
      .split('; ')
      .find(row => row.startsWith('token='))
      ?.split('=')[1];
    return token || null;
  }

  // Helper method to get headers with token
  private getHeaders(): HttpHeaders {
    const token = this.getToken();
    if (!token) {
      throw new Error('Authorization token is missing');
    }
    return new HttpHeaders().set('Authorization', `Bearer ${token}`);
  }

  // Add participation to an event
  addParticipation(participationData: any): Observable<any> {
    const headers = this.getHeaders();
    return this.http.post(`${this.apiUrl}/add`, participationData, { headers })
      .pipe(
        catchError(this.handleError) // Add error handling
      );
  }

  // Fetch all participations
  getAllParticipations(): Observable<any> {
    const headers = this.getHeaders();
    return this.http.get(`${this.apiUrl}`, { headers })
      .pipe(
        catchError(this.handleError) // Add error handling
      );
  }

  // Fetch a participation by ID
  getParticipationById(id: string): Observable<any> {
    const headers = this.getHeaders();
    return this.http.get(`${this.apiUrl}/${id}`, { headers })
      .pipe(
        catchError(this.handleError) // Add error handling
      );
  }

  // In ParticipationService
deleteParticipation(id: string): Observable<any> {
  const headers = this.getHeaders();
  return this.http.delete(`${this.apiUrl}/${id}`, { headers, responseType: 'text' })
    .pipe(
      catchError(this.handleError) // Add error handling
    );
}


  // Get participations by event ID
  getParticipationByEvent(eventId: string): Observable<any> {
    const headers = this.getHeaders();
    return this.http.get(`${this.apiUrl}/event/${eventId}`, { headers })
      .pipe(
        catchError(this.handleError) // Add error handling
      );
  }

  // Handle errors
  private handleError(error: any): Observable<never> {
    let errorMessage = 'An unknown error occurred!';
    if (error.error instanceof ErrorEvent) {
      // Client-side error
      errorMessage = `Error: ${error.error.message}`;
    } else {
      // Server-side error
      errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;
    }
    return throwError(errorMessage); // Propagate the error to the component
  }
}
