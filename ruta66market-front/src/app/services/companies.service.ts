import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { Company } from '../models/Company';
import { development } from '../models/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CompaniesService {


  private dev = development.url;

  constructor(private http: HttpClient) { }

  getCompanyByOwnerId(id: number | undefined): Observable<Company> {
    return this.http.get<Company>(`${this.dev}/company/from-user/${id}`).pipe(
      tap(data => console.log(`%c[GET] /company/from-user/${id} %cEmpresa obtenida:`, 'color: #3b82f6; font-weight: bold', 'color: gray', data))
    );
  }

  registerNewCompany(newCompany: Company): Observable<Company> {
    console.log('Registrando nueva empresa: SERVICIO', newCompany);
    return this.http.post<Company>(`${this.dev}/company/create`, newCompany).pipe(
      tap(data => console.log(`%c[POST] /company/create %cEmpresa registrada:`, 'color: #10b981; font-weight: bold', 'color: gray', data))
    )
  }
}
