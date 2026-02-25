import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

export const roleGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);

  // Leemos el rol que exige la ruta a la que intenta entrar
  const expectedRole = route.data?.['expectedRole'];

  // Leemos el rol actual del usuario desde su sesión
  const currentRole = localStorage.getItem('userRole');

  // Si el rol coincide exactamente, le abrimos la puerta
  if (currentRole === expectedRole) {
    return true;
  }

  // Si intenta entrar a un sitio donde no debe, lo mandamos a su panel
  if (currentRole === 'CLIENTE') {
    router.navigate(['/customer/dashboard']);
  } else if (currentRole === 'ADMIN_NEGOCIO') {
    router.navigate(['/business/dashboard']);
  } else {
    // Si su rol es rarísimo o no existe, al login por seguridad
    router.navigate(['/login']);
  }

  return false;
};
