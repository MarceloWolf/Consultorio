import { Component, EventEmitter, Inject, Input, Output } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { RoleEnum } from 'src/app/core/models/user.model';
import { AuthService } from 'src/app/core/services/auth.service';
import { UserService } from 'src/app/core/services/user.service';

@Component({
  selector: 'app-confirm-dialog',
  templateUrl: './confirm-dialog.component.html',
  styleUrls: ['./confirm-dialog.component.css']
})
export class ConfirmDialogComponent {

  username: string;
  newPassword:string = '';
  availableToCancel:boolean = true;
  constructor(public dialogRef: MatDialogRef<ConfirmDialogComponent>,@Inject(MAT_DIALOG_DATA) public data: { username: string },private _authService:AuthService, private _userService:UserService) {
    this.username = data.username;
  }

  validateCancel()
  {
    const token = this._authService.getInfoToken();
    
        this._userService.getUserByUsername(token.sub).subscribe({
          next:(response) => {
            if(response.role !== RoleEnum.ADMIN && response.newAccount === true)
            {
              this.availableToCancel = false;
            }
          }
        })
  }


  send(){
    this.dialogRef.close(this.newPassword);
  }

  cancel(){
    this.dialogRef.close();
  }

}
