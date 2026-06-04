import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProfessionalViewComponent } from './professional-view/professional-view.component';
import { SharedModule } from "../shared/shared.module";
import { ProfessionalRoutingModule } from './professional-routing.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AddConsultationComponent } from './add-consultation/add-consultation.component';
import { ConsultationDetailComponent } from './consultation-detail/consultation-detail.component';



@NgModule({
  declarations: [
    ProfessionalViewComponent,
    AddConsultationComponent,
    ConsultationDetailComponent
  ],
  imports: [
    CommonModule,
    ProfessionalRoutingModule,
    ReactiveFormsModule,
    SharedModule,
    FormsModule
]
})
export class ProfessionalModule { }
