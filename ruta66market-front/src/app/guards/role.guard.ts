import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

export const roleGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);

  const expectedRole = route.data?.['expectedRole'];

  const currentRole = localStorage.getItem('userRole');

  if (currentRole === expectedRole) {
    return true;
  }

  if (currentRole === 'CLIENTE') {
    router.navigate(['/customer/dashboard']);
  } else if (currentRole === 'ADMIN_NEGOCIO') {
    router.navigate(['/business/dashboard']);
  } else {
    router.navigate(['/login']);
  }

  return false;
};
