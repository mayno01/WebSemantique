import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReservationService {
  private baseUrl = 'http://localhost:8082/reservations'; // Adjust port if necessary

  constructor(private http: HttpClient) {}

  // Retrieve all reservations
  getAllReservations(): Observable<string> {
    return this.http.get<string>(this.baseUrl, {
      headers: new HttpHeaders({
        'Content-Type': 'application/json'
      })
    });
  }

  
  addReservation(reservationData: {
    Reservation_id: string;
    Reservation_type: string;
    Reservation_date: string;
    Reservation_description: string;
    Reservation_feedback: string;
  }): Observable<string> {
    return this.http.post<string>(this.baseUrl, reservationData, { responseType: 'text' as 'json' });
  }

  // Update an existing reservation
  updateReservation(id: string, reservationData: {
    Reservation_type: string;
    Reservation_date: string;
    Reservation_description: string;
    Reservation_feedback: string;
  }): Observable<string> {
    return this.http.put<string>(`${this.baseUrl}/${id}`, reservationData, { responseType: 'text' as 'json' });
  }

  // Delete a reservation
  deleteReservation(id: string): Observable<string> {
    return this.http.delete<string>(`${this.baseUrl}/${id}`, { responseType: 'text' as 'json' });
  }
}
