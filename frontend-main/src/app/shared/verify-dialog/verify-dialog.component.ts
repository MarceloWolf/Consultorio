import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-verify-dialog',
  templateUrl: './verify-dialog.component.html',
  styleUrls: ['./verify-dialog.component.css']
})
export class VerifyDialogComponent {

  constructor(public dialogRef: MatDialogRef<VerifyDialogComponent>) {
    }

  positiveResponse(){
    this.dialogRef.close(true);
  }

  negativeResponse(){
    this.dialogRef.close(false);
  }
}
