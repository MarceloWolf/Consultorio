import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PatientViewComponent } from './components/patient-view/patient-view.component';

const routes: Routes = [
  { path: '', component: PatientViewComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PatientRoutingModule { }
