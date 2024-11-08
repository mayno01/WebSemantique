import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { jwtDecode } from "jwt-decode";

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
    const token = this.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    return this.http.get(`${this.apiUrl}/all`, { headers });
  }

  
  getUserById(id: string): Observable<any> {
    const token = this.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
  
    return this.http.get(`${this.apiUrl}/${id}`, { headers });
  }
  

  getUserIdFromToken(): string | null {
    const token = this.getToken();
    if (token) {
      try {
        const decoded: any = jwtDecode(token);  
        return decoded.userId;  
      } catch (error) {
        console.error("Failed to decode token:", error);
        return null;
      }
    }
    return null;
  }
  updateUserGroup(userId: string, groupId: string): Observable<any> {
    const token = this.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    const payload = { groupId }; 
  
    return this.http.put(`${this.apiUrl}/${userId}/group`, payload , { headers ,responseType: 'text'  } ,);
  }
  

}
