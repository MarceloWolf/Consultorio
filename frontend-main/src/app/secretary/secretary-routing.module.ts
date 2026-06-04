import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SecretaryViewComponent } from './components/secretary-view/secretary-view.component';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  { path: '', component: SecretaryViewComponent  } 
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SecretaryRoutingModule { }
