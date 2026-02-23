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

  redeemPointsFromCard(payload: { userId: number, storeId: number, points: number }): Observable<LoyaltyCard> {
    return this.http.post<LoyaltyCard>(`${this.dev}/loyalty/redeem-points`, payload);
  }
}
