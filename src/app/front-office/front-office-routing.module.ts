import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {HomeComponent} from "./home/home.component";
import { ReclamationsComponent } from './reclamations/reclamations.component';
import { ResponseViewComponent } from './response-view/response-view.component';
import { AddReclamationComponent } from './add-reclamation/add-reclamation.component';

const routes: Routes = [

  { path: '', component: HomeComponent },
  { path: 'reclamations', component: ReclamationsComponent },
  { path: 'reclamations/:reclamationId/responses', component: ResponseViewComponent },
  { path: 'reclamations/add', component: AddReclamationComponent }, // New route for adding reclamations

  // { path: '/reclamtions', component: ReclamationsComponent },


];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class FrontOfficeRoutingModule { }
