import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from './header/header.component';
import { ConfirmDialogComponent } from './confirm-dialog/confirm-dialog.component';
import { MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { AddProfessionalDialogComponent } from './add-professional-dialog/add-professional-dialog.component';
import { VerifyDialogComponent } from './verify-dialog/verify-dialog.component';


@NgModule({
  declarations: [HeaderComponent, ConfirmDialogComponent, AddProfessionalDialogComponent, VerifyDialogComponent],
  imports: [CommonModule, MatDialogModule, MatButtonModule,FormsModule,ReactiveFormsModule],
  exports: [ConfirmDialogComponent, MatDialogModule, MatButtonModule,HeaderComponent],
})
export class SharedModule {}
