import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from 'src/app/core/services/auth.service';
import { UserService } from '../../core/services/user.service';
import { RoleEnum, User } from '../../core/models/user.model';
import { Observable, of, switchMap } from 'rxjs';

export const roleRouteGuard: CanActivateFn = (route, state): Observable<boolean> => {

  const authService = inject(AuthService);
  const userService = inject(UserService);
  const router = inject(Router);

  if (!authService.isAuthenticated()) {
    return of(true);
  }

  const tokenInfo = authService.getInfoToken();

  return userService.getUserByUsername(tokenInfo.sub).pipe(
    switchMap((response: User) => {
      const user: User = response;
    
      if (route.url[0]?.path === 'login') {
        if (user.role === RoleEnum.PROFESSIONAL) {
          router.navigate(['/professional']);
        } else if (user.role === RoleEnum.SECRETARY) {
          router.navigate(['/secretary']);
        } else if (user.role === RoleEnum.ADMIN) {
          router.navigate(['/admin']);
        }
        return of(false);  
      }
      
      if (user.role === RoleEnum.PROFESSIONAL) {
        if (route.url[0]?.path !== 'professional') {
          router.navigate(['/professional']);
          return of(false);  
        }
        return of(true);  
      }

      if (user.role === RoleEnum.SECRETARY) {
        if (route.url[0]?.path !== 'secretary') {
          router.navigate(['/secretary']);
          return of(false);  
        }
        return of(true);  
      }

      if (user.role === RoleEnum.ADMIN) {
        return of (true); 
      }

      return of(false);
    })
  );
};