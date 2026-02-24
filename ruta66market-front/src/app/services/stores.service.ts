import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { development } from '../models/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class StoresService {
  private dev = development.url;

  constructor(private http: HttpClient) { }

  // Obtener todas las tiendas (Para la Landing Page)
  getAllStores(): Observable<any[]> {
    return this.http.get<any[]>(`${this.dev}/stores`).pipe(
      tap(data => console.log('%c[GET] /stores %cEstructura recibida:', 'color: #0ea5e9; font-weight: bold', 'color: gray', data))
    );
  }

  // Obtener tienda por ID del dueño/empresa
  getStoreByOwnerId(ownerId: number): Observable<any> {
    return this.http.get<any>(`${this.dev}/stores/owner/${ownerId}`).pipe(
      tap(data => console.log(`%c[GET] /stores/owner/${ownerId} %cDatos:`, 'color: #0ea5e9; font-weight: bold', 'color: gray', data))
    );
  }

  // Crear o actualizar una tienda (Para el StoreConfigComponent)
  saveStore(storeData: any): Observable<any> {
    return this.http.post<any>(`${this.dev}/stores`, storeData).pipe(
      tap(data => console.log('%c[POST] /stores %cRespuesta del servidor:', 'color: #10b981; font-weight: bold', 'color: gray', data))
    );
  }
}
