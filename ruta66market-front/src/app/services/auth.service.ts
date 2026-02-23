import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { development, production } from '../models/environments/environment';


@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private dev = development.url;
  private __prod = production.url;

  constructor(
    private http: HttpClient,
    private router: Router
  ) { }

  signup(email: String, name: String, password: String) {
    return this.http.post<any>(`${this.dev}/auth/sign-up`, { "email": email, "name": name, "password": password });
  }

  login(email: String, password: String) {
    return this.http.post<any>(`${this.dev}/auth/log-in`, { "email": email, "password": password });
  }

  logout() {
    localStorage.removeItem('token');
    this.router.navigate(["/log-in"])
    return;
  }

  validateToken(token: any) {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.get<any>(`${this.dev}/auth/validate`, { headers });
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }
  getHeaders(): HttpHeaders | null {
    const token = this.getToken();
    let headers = new HttpHeaders();
    if (token) {
      headers = headers.set('Authorization', `Bearer ${token}`);
    }
    return headers;
  }
}
