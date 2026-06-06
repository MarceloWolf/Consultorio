import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from 'src/app/core/services/auth.service';
import { RoleEnum } from '../../core/models/user.model';

export const roleRouteGuard: CanActivateFn = (route, state): boolean => {

  const authService = inject(AuthService);
  const router = inject(Router);

  if (!authService.isAuthenticated()) {
    return true;
  }

  const tokenInfo = authService.getInfoToken();
  if (!tokenInfo || !tokenInfo.sub || !tokenInfo.role) {
    authService.logOut();
    router.navigate(['/login']);
    return false;
  }

  const userRole = tokenInfo.role;

  if (route.url[0]?.path === 'login') {
    if (userRole === RoleEnum.PROFESSIONAL) {
      router.navigate(['/professional']);
    } else if (userRole === RoleEnum.SECRETARY) {
      router.navigate(['/secretary']);
    } else if (userRole === RoleEnum.ADMIN) {
      router.navigate(['/admin']);
    }
    return false;  
  }
  
  if (userRole === RoleEnum.PROFESSIONAL) {
    if (route.url[0]?.path !== 'professional' && route.url[0]?.path !== 'patient') {
      router.navigate(['/professional']);
      return false;  
    }
    return true;  
  }

  if (userRole === RoleEnum.SECRETARY) {
    if (route.url[0]?.path !== 'secretary' && route.url[0]?.path !== 'patient') {
      router.navigate(['/secretary']);
      return false;  
    }
    return true;  
  }

  if (userRole === RoleEnum.ADMIN) {
    return true; 
  }

  return false;
};