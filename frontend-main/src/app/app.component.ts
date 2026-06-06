import { Component, OnInit } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { filter } from 'rxjs/operators';
import { AuthService } from './core/services/auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'frontend-main';
  showLayout = false;

  constructor(private router: Router, private authService: AuthService) {}

  ngOnInit() {
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe((event: any) => {
      // Hide header/sidebar on login page or if not authenticated
      const isLogin = event.urlAfterRedirects.includes('/login') || event.urlAfterRedirects === '/';
      this.showLayout = !isLogin && this.authService.isAuthenticated();
    });
  }
}
