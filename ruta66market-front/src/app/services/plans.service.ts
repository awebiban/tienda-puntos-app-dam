import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { development } from '../models/environments/environment';
import { Plan } from '../models/Plan';

@Injectable({
  providedIn: 'root'
})
export class PlansService {

  private dev = development.url;

  constructor(private http: HttpClient) { }

  getAllActivePlans(): Observable<Plan[]> {
    return this.http.get<Plan[]>(`${this.dev}/plans`).pipe(
      tap(data => console.log(`%c[GET] /plans %cLista de planes:`, 'color: #8b5cf6; font-weight: bold', 'color: gray', data))
    );
  }
}
