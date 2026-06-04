import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/core/services/auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit{

  loggedInUser:string = '';
  
  constructor(private authService:AuthService, private router:Router){}
  
  ngOnInit(): void {
    const token = this.authService.getInfoToken();
    this.loggedInUser = token.sub;
  }

  logOut()
  {
     this.authService.logOut();
     this.router.navigate(['/login']);
  }

}
