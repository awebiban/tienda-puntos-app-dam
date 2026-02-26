import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class CompaniesService {

<<<<<<< Updated upstream
  constructor() { }
=======
  private dev = development.url;

  constructor(private http: HttpClient) { }

  registerNewCompany(newCompany: Company): Observable<Company> {
    console.log('Registrando nueva empresa: SERVICIO', newCompany);
    return this.http.post<Company>(`${this.dev}/company/create`, newCompany).pipe(
      tap(data => console.log(`%c[POST] /company/create %cEmpresa registrada:`, 'color: #10b981; font-weight: bold', 'color: gray', data))
    )
  }

  findCompanyByOwnerId(ownerId: number): Observable<Company> {
    return this.http.get<Company>(`${this.dev}/company/from-user/${ownerId}`).pipe(
      tap(data => console.log(`%c[GET] /company/from-user/${ownerId} %cEmpresa encontrada:`, 'color: #10b981; font-weight: bold', 'color: gray', data))
    );
  }
>>>>>>> Stashed changes
}
