import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { tap } from 'rxjs';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const token = authService.getToken();

  // Clonamos la petición para añadir el encabezado si el token existe
  let authReq = req;
  if (token) {
    authReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
    console.log(`%c[HTTP Interceptor] %cAñadiendo token a: %c${req.url}`,
                'color: #6366f1; font-weight: bold',
                'color: #94a3b8',
                'color: #38bdf8');
  }

  // Pasamos la petición y usamos tap para ver la respuesta del Back
  return next(authReq).pipe(
    tap({
      next: (event: any) => {
        if (event.type !== 0) { // Ignoramos eventos de carga (Sent)
           console.log(`%c[Back-end Response] %cDatos recibidos de ${req.url.split('/').pop()}:`,
                       'color: #10b981; font-weight: bold',
                       'color: #94a3b8',
                       event.body);
        }
      },
      error: (err: any) => {
        console.error(`%c[HTTP Error] %cError en la petición a ${req.url}:`,
                      'color: #ef4444; font-weight: bold',
                      'color: #94a3b8',
                      err);
                      if (err.status === 401) {
            console.warn("Token expirado o inválido. Cerrando sesión...");
            authService.logout();
        }
      }
    })
  );
};
