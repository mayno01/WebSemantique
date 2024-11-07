
import { provideRouter, Routes } from '@angular/router';


export const routes: Routes = [
  { path: 'register', loadChildren: () => import('./register/register.module').then(m => m.RegisterModule) },
  { path: 'home', loadChildren: () => import('./front-office/front-office.module').then(m => m.FrontOfficeModule) },
  { path: 'Admin', loadChildren: () => import('./back-office/back-office.module').then(m => m.BackOfficeModule) },
  { path: 'login', loadChildren: () => import('./login/login.module').then(m => m.LoginModule) },

];
export const AppConfig = [
  provideRouter(routes),
];