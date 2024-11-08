import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class CommentaireService {
  private apiUrl = 'http://localhost:8082/commentaires';

  constructor(private http: HttpClient, private authService: AuthService) {}

  private getHeaders(): HttpHeaders {
    const token = this.authService.getToken();
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  }

  getCommentairesByBlog(blogId: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/${blogId}`, { headers: this.getHeaders() });
  }

  addCommentaire(blogId: string, commentaireData: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/${blogId}`, commentaireData, { headers: this.getHeaders(), responseType: 'text' });
  }

  deleteCommentaire(commentaireId: string): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${commentaireId}`, { headers: this.getHeaders(), responseType: 'text' });
  }
}
