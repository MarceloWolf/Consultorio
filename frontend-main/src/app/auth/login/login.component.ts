import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AccountStateEnum, User } from 'src/app/core/models/user.model';
import { AuthService } from 'src/app/core/services/auth.service';
import { UserService } from 'src/app/core/services/user.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  showPassword: boolean = false;
  user?:User;


  constructor(private fb:FormBuilder,
    private http:HttpClient,
    private router:Router,
    private _authService:AuthService,
    private userService:UserService
  ){
    this.loginForm = this.fb.group({
      username:['', [Validators.required]],
      password:['', [Validators.required, Validators.maxLength(100)]]

    })
  }

  ngOnInit(): void {
  }
  
  togglePasswordVisibility(): void {
    this.showPassword = !this.showPassword;
  }

  onSubmit(): void {
    if (this.loginForm.valid) {
        const { username, password } = this.loginForm.value;

        this.userService.getUserByUsername(username).subscribe({
            next: (user) => {
                if (user.accountState === AccountStateEnum.INACTIVE) {
                    alert('El usuario se encuentra dado de baja, por lo tanto no puede ingresar al sistema');
                    return;
                }

                this._authService.login(username, password).subscribe({
                    next: () => {
                        this.user = user; 

                        switch (this.user?.role) {
                            case 'ADMIN':
                                this.router.navigate(['/admin']);
                                break;
                            case 'SECRETARY':
                                this.router.navigate(['/secretary']);
                                break;
                            case 'PROFESSIONAL':
                                this.router.navigate(['/professional']); 
                                break;
                            default:
                                alert('Rol no reconocido');
                        }
                    },
                    error: () => alert('Usuario o contraseña incorrectos'),
                });
            },
            error: () => alert('Usuario o contraseña incorrectos'),
        });
    }
}

}
