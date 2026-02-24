import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { development } from '../models/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class RewardsService {
  private dev = development.url;

  constructor(private http: HttpClient) { }

  // Obtener todos los premios de una tienda en concreto
  getRewardsByStoreId(storeId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.dev}/rewards/store/${storeId}`).pipe(
      tap(data => console.log(`%c[GET] /rewards/store/${storeId} %cLista de premios:`, 'color: #8b5cf6; font-weight: bold', 'color: gray', data))
    );
  }

  // Crear una nueva recompensa
  createReward(rewardData: any): Observable<any> {
    return this.http.post<any>(`${this.dev}/rewards`, rewardData).pipe(
      tap(data => console.log('%c[POST] /rewards %cPremio creado:', 'color: #10b981; font-weight: bold', 'color: gray', data))
    );
  }

  // Eliminar una recompensa
  deleteReward(rewardId: number): Observable<any> {
    return this.http.delete<any>(`${this.dev}/rewards/${rewardId}`).pipe(
      tap(() => console.log(`%c[DELETE] /rewards/${rewardId} %cPremio eliminado correctamente`, 'color: #ef4444; font-weight: bold', 'color: gray'))
    );
  }
}
