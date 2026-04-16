import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthStateService } from '../state/auth-state.service';

export const authGuard: CanActivateFn = () => {
  const auth = inject(AuthStateService);
  const router = inject(Router);
  if (auth.isLoggedIn()) return true;
  return router.createUrlTree(['/login']);
};

export const adminGuard: CanActivateFn = () => {
  const auth = inject(AuthStateService);
  const router = inject(Router);
  if (auth.isAdmin()) return true;
  return router.createUrlTree(['/operator/dashboard']);
};

export const operatorGuard: CanActivateFn = () => {
  const auth = inject(AuthStateService);
  const router = inject(Router);
  if (auth.isOperator()) return true;
  return router.createUrlTree(['/admin/dashboard']);
};

export const guestGuard: CanActivateFn = () => {
  const auth = inject(AuthStateService);
  const router = inject(Router);
  if (!auth.isLoggedIn()) return true;
  return auth.isAdmin()
    ? router.createUrlTree(['/admin/dashboard'])
    : router.createUrlTree(['/operator/dashboard']);
};
