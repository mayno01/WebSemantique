
import { provideRouter, Routes } from '@angular/router';
import { AddFormationComponent } from './components/add-formation/add-formation.component';
import { FormationComponent } from './components/formation/formation.component';
import { InscriptionComponent } from './components/inscription/inscription.component';
import { UpdateFormationComponent } from './components/update-formation/update-formation.component';
import { AddInscriptionComponent } from './components/add-inscription/add-inscription.component';
import { FormationfrontComponent } from './components/formationfront/formationfront.component';
import { ReclamationsComponent } from './front-office/reclamations/reclamations.component'; 
import { ResponseViewComponent } from './front-office/response-view/response-view.component';
import { AddReclamationComponent } from './front-office/add-reclamation/add-reclamation.component';
import { EquipeComponent } from './back-office/equipe/equipe.component'; 
import { ReservationComponent } from './back-office/reservation/reservation.component';




export const routes: Routes = [
  { path: 'register', loadChildren: () => import('./register/register.module').then(m => m.RegisterModule) },
  { path: '', loadChildren: () => import('./front-office/front-office.module').then(m => m.FrontOfficeModule) },
  { path: 'Admin', loadChildren: () => import('./back-office/back-office.module').then(m => m.BackOfficeModule) },
  { path: 'login', loadChildren: () => import('./login/login.module').then(m => m.LoginModule) },
  {path:"allusers",loadChildren:()=>import('./back-office/all-users/all-users.module').then(m=>m.AllUsersModule)},
{ path:'userProfile',loadChildren:()=>import('./front-office/user-profile/user-profile.module').then(m=>m.UserProfileModule)},
{ path: 'edit-user/:id', loadChildren: () => import('./front-office/edit-user/edit-user.module').then(m => m.EditUserModule) },

{ path: 'addformation', component: AddFormationComponent },
{ path: 'formations', component: FormationComponent},
{ path: 'inscriptions', component: InscriptionComponent },
{ path: 'updateformation/:id', component: UpdateFormationComponent },
{ path: 'addinscription', component: AddInscriptionComponent },
{ path: 'formationsfront', component: FormationfrontComponent },



{ path: 'reclamations', component: ReclamationsComponent },
{ path: 'reclamations/:reclamationId/responses', component: ResponseViewComponent },
{ path: 'reclamations/add', component: AddReclamationComponent },


{ path: 'equipe', component: EquipeComponent },
{ path: 'reservation', component: ReservationComponent }


];
export const AppConfig = [
  provideRouter(routes),
];