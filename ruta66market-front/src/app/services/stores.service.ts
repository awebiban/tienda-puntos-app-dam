import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { development, production } from '../models/environments/environment';
import { Store } from '../models/Store';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class StoresService {

  private dev = development.url;
  private __prod = production.url;

  constructor(
    private http: HttpClient,
    private router: Router,
    private authService: AuthService,
  ) { }

  getAllStores(): Observable<Store[]> {
    return this.http.get<Store[]>(`${this.dev}/store/`);
  }

  getStoreById(id: string): Observable<Store> {
    return this.http.get<Store>(`${this.dev}/store/${id}`);
  }

  getStoresByCompanyId(companyId: number): Observable<Store[]> {
    return this.http.get<Store[]>(`${this.dev}/store/company/${companyId}`);
  }

  createStore(storeData: Store): Observable<Store> {
    return this.http.post<Store>(`${this.dev}/store/create`, storeData);
  }

  updateStore(id: number, storeData: Store): Observable<Store> {
    return this.http.put<Store>(`${this.dev}/store/update/${id}`, storeData);
  }

  setVisibilityOff(id: number): Observable<Store> {
    return this.http.get<Store>(`${this.dev}/store/disable/${id}`)
  }

  setVisibilityOn(id: number): Observable<Store> {
    return this.http.get<Store>(`${this.dev}/store/activate/${id}`)
  }
}
