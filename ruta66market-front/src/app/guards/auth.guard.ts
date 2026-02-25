import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  // Verificamos si hay token en el localStorage
  if (authService.isLoggedIn()) {
    return true; // Puede pasar
  }

  // Si no está logueado, lo expulsamos al login
  router.navigate(['/login']);
  return false;
};
