import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProfessionalViewComponent } from './professional-view/professional-view.component';
import { RouterModule, Routes } from '@angular/router';



const routes: Routes = [
  { path: '', component: ProfessionalViewComponent  } 
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ProfessionalRoutingModule { }
