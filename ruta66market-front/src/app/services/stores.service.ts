import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { development } from '../models/environments/environment';
import { Store } from '../models/Store';

@Injectable({
  providedIn: 'root'
})
export class StoresService {

  private dev = development.url;

  constructor(private http: HttpClient) { }

  // Obtener todas las tiendas (Para la Landing Page)
  getAllStores(): Observable<any[]> {
    return this.http.get<any[]>(`${this.dev}/store`).pipe(
      tap(data => console.log('%c[GET] /store %cEstructura recibida:', 'color: #0ea5e9; font-weight: bold', 'color: gray', data))
    );
  }

  // Obtener tienda por ID
  getStoreById(storeId: number): Observable<Store> {
    return this.http.get<Store>(`${this.dev}/store/${storeId}`).pipe(
      tap(data => console.log('%c[GET] /store/%d %cDatos:', 'color: #0ea5e9; font-weight: bold', 'color: gray', storeId, data))
    );
  }

  // Obtener tienda por ID del dueño/empresa
  getStoreByOwnerId(ownerId: number): Observable<any> {
    return this.http.get<any>(`${this.dev}/store/owner/${ownerId}`).pipe(
      tap(data => console.log(`%c[GET] /store/owner/${ownerId} %cDatos:`, 'color: #0ea5e9; font-weight: bold', 'color: gray', data))
    );
  }

  // Crear o actualizar una tienda (Para el StoreConfigComponent)
  saveStore(storeData: any): Observable<any> {
    return this.http.post<any>(`${this.dev}/store`, storeData).pipe(
      tap(data => console.log('%c[POST] /store %cRespuesta del servidor:', 'color: #10b981; font-weight: bold', 'color: gray', data))
    );
  }
}
