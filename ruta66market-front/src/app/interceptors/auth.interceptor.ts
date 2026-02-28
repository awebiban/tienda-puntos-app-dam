import { HttpErrorResponse, HttpInterceptorFn, HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, of, throwError } from 'rxjs'; // 👈 Importamos 'of' y 'catchError'
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
      // 🕵️ COMPROBACIÓN SILENCIOSA:
      // Si es el error 403 de la compañía, lo tratamos como un éxito vacío
      if (err.status === 403 && req.url.includes('/api/company/from-user/')) {
        console.log(`%c[Merchant Info] %cUsuario sin empresa (Flujo planificado)`,
          'color: #6366f1; font-weight: bold', 'color: #94a3b8');

        // Devolvemos una respuesta 200 con cuerpo null para que no salte en rojo
        return of(new HttpResponse({ body: null, status: 200 }));
      }

      // 🚨 SÓLO AQUÍ imprimimos errores reales en rojo
      // Al mover el console.error dentro de este bloque (fuera del IF anterior), 
      // el error 403 de la compañía ya no se imprimirá como error.
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