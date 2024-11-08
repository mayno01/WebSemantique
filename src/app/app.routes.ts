
import { provideRouter, Routes } from '@angular/router';
import { EquipeComponent } from './back-office/equipe/equipe.component';
import { ReservationComponent } from './back-office/reservation/reservation.component';


export const routes: Routes = [
  { path: 'register', loadChildren: () => import('./register/register.module').then(m => m.RegisterModule) },
  { path: 'home', loadChildren: () => import('./front-office/front-office.module').then(m => m.FrontOfficeModule) },
  { path: 'Admin', loadChildren: () => import('./back-office/back-office.module').then(m => m.BackOfficeModule) },
  { path: 'login', loadChildren: () => import('./login/login.module').then(m => m.LoginModule) },
  { path: 'equipe', component: EquipeComponent },
  { path: 'reservation', component: ReservationComponent }

];
export const AppConfig = [
  provideRouter(routes),
];
