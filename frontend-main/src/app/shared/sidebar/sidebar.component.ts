import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/core/services/auth.service';
import { RoleEnum } from 'src/app/core/models/user.model';

interface NavItem {
  label: string;
  route: string;
  icon: string;
  allowedRoles: RoleEnum[];
}

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent implements OnInit {
  isCollapsed = false;
  userRole: RoleEnum | null = null;
  loggedInUser = '';

  navItems: NavItem[] = [
    {
      label: 'Panel Admin',
      route: '/admin',
      icon: 'admin_panel_settings',
      allowedRoles: [RoleEnum.ADMIN]
    },
    {
      label: 'Agenda de Turnos',
      route: '/secretary',
      icon: 'calendar_month',
      allowedRoles: [RoleEnum.ADMIN, RoleEnum.SECRETARY]
    },
    {
      label: 'Fichas Profesionales',
      route: '/professional',
      icon: 'medical_services',
      allowedRoles: [RoleEnum.ADMIN, RoleEnum.PROFESSIONAL]
    },
    {
      label: 'Portal Pacientes (Mock)',
      route: '/patient',
      icon: 'badge',
      allowedRoles: [RoleEnum.ADMIN, RoleEnum.PROFESSIONAL, RoleEnum.SECRETARY]
    }
  ];

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    const tokenInfo = this.authService.getInfoToken();
    if (tokenInfo) {
      this.userRole = tokenInfo.role;
      this.loggedInUser = tokenInfo.sub;
    }
  }

  toggleSidebar() {
    this.isCollapsed = !this.isCollapsed;
  }

  isAllowed(item: NavItem): boolean {
    return this.userRole ? item.allowedRoles.includes(this.userRole) : false;
  }

  isActive(route: string): boolean {
    return this.router.url.startsWith(route);
  }

  navigate(route: string) {
    this.router.navigate([route]);
  }

  translateRole(role: string | null): string {
    if (!role) return '';
    switch (role) {
      case 'ADMIN': return 'Administrador';
      case 'SECRETARY': return 'Secretario/a';
      case 'PROFESSIONAL': return 'Profesional';
      default: return role;
    }
  }
}
