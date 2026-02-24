import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { development } from '../models/environments/environment';
import { Transaction } from '../models/Transaction';

@Injectable({
  providedIn: 'root'
})
export class TransactionsService {

  private dev = development.url;

  constructor(private http: HttpClient) { }

  getTransactionsByUserId(loyaltyCardId: number): Observable<Transaction[]> {
    console.log(`%c[Request] %cSolicitando historial para Card ID: ${loyaltyCardId}`, 'color: #f59e0b; font-weight: bold', 'color: #94a3b8');
    return this.http.get<Transaction[]>(`${this.dev}/loyalty/history/${loyaltyCardId}`);
  }
}
