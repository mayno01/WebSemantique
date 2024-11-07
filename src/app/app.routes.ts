import { Routes } from '@angular/router';
import { ResponseViewComponent } from './front-office/response-view/response-view.component';
import { AddReclamationComponent } from './front-office/add-reclamation/add-reclamation.component';
import { ReclamationService } from './services/reclamation.service';
import { ReclamationsComponent } from './front-office/reclamations/reclamations.component';
import { AllReclamationComponent } from './back-office/all-reclamation/all-reclamation.component';

export const routes: Routes = [

  { path: '', loadChildren: () => import('./front-office/front-office.module').then(m => m.FrontOfficeModule) },
  { path: 'Admin', loadChildren: () => import('./back-office/back-office.module').then(m => m.BackOfficeModule) },
  { path: 'reclamations', component: ReclamationsComponent },
  { path: 'reclamations/:reclamationId/responses', component: ResponseViewComponent },
  { path: 'reclamations/add', component: AddReclamationComponent }, // New route for adding reclamations
  // { path: 'a', component: AllReclamationComponent },


];
