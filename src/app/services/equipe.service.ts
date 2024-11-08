import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class EquipeService {
  private baseUrl = 'http://localhost:8082/equipes'; // Adjust port if necessary

  constructor(private http: HttpClient) {}

  // Retrieve all equipes
  getAllEquipes(): Observable<string> {
    return this.http.get<string>(this.baseUrl, {
      headers: new HttpHeaders({
        'Content-Type': 'application/json'
      })
    });
  }

  // Add a new equipe
  addEquipe(equipeData: { Equipe_id: string; Equipe_type: string }): Observable<string> {
    return this.http.post<string>(this.baseUrl, equipeData, { responseType: 'text' as 'json' });
  }


  // Update an existing equipe
  updateEquipe(id: string, equipeData: { Equipe_type: string }): Observable<string> {
    return this.http.put<string>(`${this.baseUrl}/${id}`, equipeData, { responseType: 'text' as 'json' });
  }


  // Delete an equipe
  deleteEquipe(id: string): Observable<string> {
    return this.http.delete<string>(`${this.baseUrl}/${id}`, { responseType: 'text' as 'json' });
  }

}
