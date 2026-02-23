import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { development, production } from '../models/environments/environment';
import { Transaction } from '../models/Transaction';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class TransactionsService {

  private dev = development.url;
  private __prod = production.url;

  constructor(
    private http: HttpClient,
    private router: Router,
    private authService: AuthService,
  ) { }

  getTransactionsByUserId(loyaltyCardId: number): Observable<Transaction[]> {
    return this.http.get<Transaction[]>(`${this.dev}/loyalty/history/${loyaltyCardId}`);
  }
}
