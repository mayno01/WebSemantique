import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8082/users';

  constructor(private http: HttpClient) { }

  register(username: string, password: string, role: string): Observable<any> {
    const payload = { username, password, role };
    return this.http.post(`${this.apiUrl}/register`, payload, { responseType: 'text' });
  }
  
  login(username: string, password: string): Observable<any> {
    const payload = { username, password };
    return this.http.post(`${this.apiUrl}/login`, payload, { responseType: 'text' });
  }

  
  setToken(token: string): void {
    const expires = new Date();
    expires.setSeconds(expires.getSeconds() + 3600); 
    document.cookie = `token=${token};expires=${expires.toUTCString()};path=/`;
  
    // Store token in localStorage
    localStorage.setItem('authToken', token);  // Save token to localStorage
    console.log('Token stored in localStorage:', token);  // Log to confirm token is stored
  }
  

  
  getToken(): string | null {
    const token = document.cookie
      .split('; ')
      .find(row => row.startsWith('token='))
      ?.split('=')[1];
    return token || null;
  }


  removeToken(): void {
    document.cookie = 'token=;expires=Thu, 01 Jan 1970 00:00:00 UTC;path=/';
  }


  
getAllUsers(): Observable<any> {
    return this.http.get(`${this.apiUrl}/all`);
  }
  
}
