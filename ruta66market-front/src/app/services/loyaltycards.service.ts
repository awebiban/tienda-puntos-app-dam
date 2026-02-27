import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { development } from '../models/environments/environment';
import { LoyaltyCard } from '../models/LoyaltyCard';

@Injectable({
  providedIn: 'root'
})
export class LoyaltycardsService {

  private dev = development.url;

  constructor(private http: HttpClient) { }

  findByCardId(cardId: number) {
    return this.http.get<LoyaltyCard>(`${this.dev}/loyalty/${cardId}`);
  }

  getAllLoyaltyCardsByUserId(userId: number): Observable<LoyaltyCard[]> {
    console.log(`%c[Request] %cSolicitando tarjetas para User ID: ${userId}`, 'color: #f59e0b; font-weight: bold', 'color: #94a3b8');
    return this.http.get<LoyaltyCard[]>(`${this.dev}/loyalty/user/${userId}`);
  }

  // NUEVO: Obtener todas las tarjetas de una tienda (Para el Merchant Dashboard)
  getCardsByStoreId(storeId: number): Observable<LoyaltyCard[]> {
    return this.http.get<LoyaltyCard[]>(`${this.dev}/loyalty/store/${storeId}`);
  }

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

  updateLastAccess(cardId: number) {
    return this.http.put<any>(`${this.dev}/loyalty/update-last-access/${cardId}`, {});
  }

  redeemPointsFromCard(payload: { userId: number, storeId: number, points: number }): Observable<LoyaltyCard> {
    return this.http.post<LoyaltyCard>(`${this.dev}/loyalty/redeem-points`, payload);

  }
}
