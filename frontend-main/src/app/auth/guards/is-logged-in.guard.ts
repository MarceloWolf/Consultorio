import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { RoleEnum } from 'src/app/core/models/user.model';
import { AuthService } from 'src/app/core/services/auth.service';

export const isLoggedInGuard: CanActivateFn = (route, state) => {

  const authService = inject(AuthService);
  const router = inject(Router);

    if (authService.isAuthenticated()) {
      return true;
    } else {
      router.navigate(['/login']);
      return false;
    }
  
};
