import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { development, production } from '../models/environments/environment';
import { LoyaltyCard } from '../models/LoyaltyCard';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class LoyaltycardsService {

  private dev = development.url;
  private __prod = production.url;

  constructor(
    private http: HttpClient,
    private router: Router,
    private authService: AuthService,
  ) { }

  getAllLoyaltyCardsByUserId(userId: number): Observable<LoyaltyCard[]> {
    return this.http.get<LoyaltyCard[]>(`${this.dev}/loyalty/user/${userId}`);
  }

  addPointsToCard(payload: { userId: number, storeId: number, points: number }): Observable<LoyaltyCard> {
    return this.http.post<LoyaltyCard>(`${this.dev}/loyalty/add-points`, payload);
  }

<<<<<<< Updated upstream
  redeemPointsFromCard(payload: { userId: number, storeId: number, points: number }): Observable<LoyaltyCard> {
    return this.http.post<LoyaltyCard>(`${this.dev}/loyalty/redeem-points`, payload);
=======
  // ACTUALIZADO: Sumar puntos directamente por ID de tarjeta
  addPoints(cardId: number, points: number): Observable<any> {
    console.log(`%c[POST] %cSumando ${points} puntos a la tarjeta ${cardId}`, 'color: #8b5cf6; font-weight: bold', 'color: gray');
    return this.http.post<any>(`${this.dev}/loyalty/add-points`, { cardId, points });
  }

  joinStore(userId: number, storeId: number): Observable<any> {
    const payload = { userId, storeId };
    console.log(`%c[POST] %cUniendo usuario ${userId} a tienda ${storeId}`, 'color: #f59e0b; font-weight: bold', 'color: gray');
    return this.http.post<any>(`${this.dev}/loyalty/join`, payload);
  }

  updateLastAccess(cardId: number | null): Observable<any> {
    if (!cardId) {
      console.warn('%c[Warning] %cNo se proporcionó cardId para actualizar último acceso', 'color: #eab308; font-weight: bold', 'color: gray');
      return new Observable(observer => {
        observer.complete();
      });
    }
    return this.http.put<any>(`${this.dev}/loyalty/update-last-access/${cardId}`, {});
>>>>>>> Stashed changes
  }
}
