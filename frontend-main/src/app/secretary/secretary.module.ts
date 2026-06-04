import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SecretaryViewComponent } from './components/secretary-view/secretary-view.component';
import { SecretaryRoutingModule } from './secretary-routing.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SharedModule } from '../shared/shared.module';
import { AddPatientComponent } from './components/add-patient/add-patient.component';
import { AddAppoinmentComponent } from './components/add-appoinment/add-appoinment.component';
import { MatDialogModule } from '@angular/material/dialog';
import { RescheduleAppointmentComponent } from './components/reschedule-appointment/reschedule-appointment.component';



@NgModule({
  declarations: [
    SecretaryViewComponent,
    AddPatientComponent,
    AddAppoinmentComponent,
    RescheduleAppointmentComponent
  ],
  imports: [
    CommonModule,
    SecretaryRoutingModule,
    ReactiveFormsModule,
    SharedModule,
    FormsModule,
    MatDialogModule
  ]
})
export class SecretaryModule { }
