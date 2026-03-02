import { HttpErrorResponse, HttpInterceptorFn, HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, of, throwError } from 'rxjs';
import { AuthService } from '../services/auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const token = authService.getToken();

  let authReq = req;
  if (token) {
    authReq = req.clone({
      setHeaders: { Authorization: `Bearer ${token}` }
    });
  }

  return next(authReq).pipe(
    catchError((err: HttpErrorResponse) => {

      if (err.status === 403 && req.url.includes('/api/company/from-user/')) {
        console.log(`%c[Merchant Info] %cUsuario sin empresa (Flujo planificado)`,
          'color: #6366f1; font-weight: bold', 'color: #94a3b8');

        return of(new HttpResponse({ body: null, status: 200 }));
      }


      console.error(`%c[HTTP Error] %cError en la petición a ${req.url}:`,
        'color: #ef4444; font-weight: bold',
        'color: #94a3b8',
        err);

      if (err.status === 401) {
        authService.logout();
      }

      return throwError(() => err);
    })
  );
};
