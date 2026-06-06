import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { PatientRoutingModule } from './patient-routing.module';
import { PatientViewComponent } from './components/patient-view/patient-view.component';
import { SharedModule } from '../shared/shared.module';

@NgModule({
  declarations: [
    PatientViewComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    PatientRoutingModule,
    SharedModule
  ]
})
export class PatientModule { }
