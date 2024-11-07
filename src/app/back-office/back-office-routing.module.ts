import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './dashboard/dashboard.component';
import { AllReclamationComponent } from './all-reclamation/all-reclamation.component';

const routes: Routes = [
  { path: '', component: DashboardComponent },
  { path: 'a', component: AllReclamationComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)], // Use forChild since it's a feature module
  exports: [RouterModule]
})
export class BackOfficeRoutingModule {}
