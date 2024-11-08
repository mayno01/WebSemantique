import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {DashboardComponent} from "./dashboard/dashboard.component";
import { EquipeComponent } from './equipe/equipe.component';

const routes: Routes = [

  { path: '', component: DashboardComponent },
  { path: 'equipe', component: EquipeComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class BackOfficeRoutingModule { }
