
import { provideRouter, Routes } from '@angular/router';
import { AddFormationComponent } from './components/add-formation/add-formation.component';
import { FormationComponent } from './components/formation/formation.component';
import { InscriptionComponent } from './components/inscription/inscription.component';
import { UpdateFormationComponent } from './components/update-formation/update-formation.component';
import { AddInscriptionComponent } from './components/add-inscription/add-inscription.component';
import { FormationfrontComponent } from './components/formationfront/formationfront.component';


export const routes: Routes = [
  { path: 'register', loadChildren: () => import('./register/register.module').then(m => m.RegisterModule) },
  { path: 'home', loadChildren: () => import('./front-office/front-office.module').then(m => m.FrontOfficeModule) },
  { path: 'Admin', loadChildren: () => import('./back-office/back-office.module').then(m => m.BackOfficeModule) },
  { path: 'login', loadChildren: () => import('./login/login.module').then(m => m.LoginModule) },
  { path: 'addformation', component: AddFormationComponent },
  { path: 'formations', component: FormationComponent},
  { path: 'inscriptions', component: InscriptionComponent },
  { path: 'updateformation/:id', component: UpdateFormationComponent },
  { path: 'addinscription', component: AddInscriptionComponent },
  { path: 'formationsfront', component: FormationfrontComponent },
];
export const AppConfig = [
  provideRouter(routes),
];