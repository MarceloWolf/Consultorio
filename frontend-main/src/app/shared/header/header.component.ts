import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/core/services/auth.service';
import { ThemeService } from 'src/app/core/services/theme.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit{

  loggedInUser:string = '';
  
  constructor(
    private authService: AuthService,
    private router: Router,
    public themeService: ThemeService
  ){}
  
  ngOnInit(): void {
    const token = this.authService.getInfoToken();
    this.loggedInUser = token ? token.sub : '';
  }

  toggleTheme() {
    this.themeService.toggleTheme();
  }

  logOut()
  {
     this.authService.logOut();
     this.router.navigate(['/login']);
  }

}

