import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, switchMap, tap } from 'rxjs';
import { development } from '../models/environments/environment';
import { Store } from '../models/Store';
import { Company } from '../models/Company';

@Injectable({
  providedIn: 'root'
})
export class StoresService {

  private dev = development.url;

  constructor(private http: HttpClient) { }

  getAllStores(): Observable<Store[]> {
    return this.http.get<Store[]>(`${this.dev}/store`);
  }

  getStoreById(storeId: number): Observable<Store> {
    return this.http.get<Store>(`${this.dev}/store/${storeId}`);
  }

  getStoreByOwnerId(ownerId: number): Observable<Store> {
    return this.http.get<Company>(`${this.dev}/company/from-user/${ownerId}`).pipe(
      switchMap((company: Company) => {
        return this.http.get<Store[]>(`${this.dev}/store/company/${company.id}`).pipe(
          tap((stores: Store[]) => console.log('%c[StoresService] %cTiendas:', 'color: #0ea5e9', 'color: gray', stores)),
          switchMap((stores: Store[]) => new Observable<Store>(obs => obs.next(stores[0])))
        );
      })
    );
  }

  // MÉTODO AÑADIDO: Para actualizar la tienda en el backend
  updateStore(id: number, store: Store): Observable<Store> {
    return this.http.put<Store>(`${this.dev}/store/update/${id}`, store);
  }

  saveStore(storeData: Store): Observable<Store> {
    return this.http.post<Store>(`${this.dev}/store/create`, storeData);
  }
}
