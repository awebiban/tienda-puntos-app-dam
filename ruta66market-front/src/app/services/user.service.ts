import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { development } from '../models/environments/environment';
import { User } from '../models/User';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private dev = development.url;

  constructor(private http: HttpClient) { }

  getUserById(userId: number): Observable<User> {
    return this.http.get<User>(`${this.dev}/user/${userId}`).pipe(
      tap(data => console.log(`%c[GET] /users/${userId} %cDatos del usuario:`, 'color: #f59e0b; font-weight: bold', 'color: gray', data))
    );
  }

  // Obtener el perfil completo del usuario por su email o ID
  getUserProfile(email: string): Observable<any> {
    return this.http.get<any>(`${this.dev}/users/profile?email=${email}`).pipe(
      tap(data => console.log(`%c[GET] /users/profile %cDatos del usuario:`, 'color: #f59e0b; font-weight: bold', 'color: gray', data))
    );
  }

  // Actualizar datos del usuario
  updateUserProfile(userId: number, userData: any): Observable<any> {
    return this.http.put<any>(`${this.dev}/users/${userId}`, userData).pipe(
      tap(data => console.log(`%c[PUT] /users/${userId} %cPerfil actualizado:`, 'color: #f59e0b; font-weight: bold', 'color: gray', data))
    );
  }
}
