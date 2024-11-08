import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class EventService {
  private apiUrl = 'http://localhost:8082/api/events'; // Backend API URL

  constructor(private http: HttpClient) {}

  // Fetch the token from cookies
  private getToken(): string | null {
    const token = document.cookie
      .split('; ')
      .find(row => row.startsWith('token='))
      ?.split('=')[1];
    return token || null;
  }

  // Fetch all events
  getAllEvents(): Observable<any> {
    const token = this.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.get(`${this.apiUrl}`, { headers });
  }

  // Fetch a single event by ID
  getEventById(id: string): Observable<any> {
    const token = this.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.get(`${this.apiUrl}/${id}`, { headers });
  }

  // Create a new event
  addEvent(eventData: any): Observable<any> {
    const token = this.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.post(`${this.apiUrl}/add`, eventData, { headers });
  }

  // Update an existing event with error handling
 // Update an existing event with error handling
updateEvent(id: string, eventData: any): Observable<any> {
  const token = this.getToken();
  const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

  // Modify the request to expect a 'text' response (since the backend returns plain text)
  return this.http.put(`${this.apiUrl}/update/${id}`, eventData, { headers, responseType: 'text' }).pipe(
    catchError(error => {
      console.error('Error status:', error.status); // Check the status code
      console.error('Error message:', error.message); // Check the error message
      console.error('Error details:', error.error); // Log the full error response body
      return throwError(error); // Propagate the error
    })
  );
}


  // Delete an event
  deleteEvent(id: string): Observable<any> {
    const token = this.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.delete(`${this.apiUrl}/${id}`, { headers });
  }

  // Get events by type
  getEventsByType(eventType: string): Observable<any> {
    const token = this.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.get(`${this.apiUrl}/type/${eventType}`, { headers });
  }
}
