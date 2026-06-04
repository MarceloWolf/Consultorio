import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { isLoggedInGuard } from './auth/guards/is-logged-in.guard';
import { roleRouteGuard } from './auth/guards/role-route.guard';

const routes: Routes = [
  { path: 'admin', loadChildren: () => import('./admin/admin.module').then(m => m.AdminModule),canActivate:[isLoggedInGuard,roleRouteGuard] },
  { path: 'secretary', loadChildren: () => import('./secretary/secretary.module').then(m => m.SecretaryModule), canActivate:[isLoggedInGuard,roleRouteGuard]},
  {path:'login', loadChildren: () => import('./auth/auth.module').then(m => m.AuthModule), canActivate:[roleRouteGuard]},
  { path:'professional', loadChildren: () => import('./professional/professional.module').then(m => m.ProfessionalModule),canActivate:[isLoggedInGuard,roleRouteGuard]},

  { path: '', pathMatch: 'full', redirectTo: 'login' },
  { path: '**', pathMatch: 'full', redirectTo: 'login' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
