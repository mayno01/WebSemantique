
import { provideRouter, Routes } from '@angular/router';


export const routes: Routes = [
  { path: 'register', loadChildren: () => import('./register/register.module').then(m => m.RegisterModule) },
  { path: 'home', loadChildren: () => import('./front-office/front-office.module').then(m => m.FrontOfficeModule) },
  { path: 'Admin', loadChildren: () => import('./back-office/back-office.module').then(m => m.BackOfficeModule) },
  { path: 'login', loadChildren: () => import('./login/login.module').then(m => m.LoginModule) },
  {path:"allusers",loadChildren:()=>import('./back-office/all-users/all-users.module').then(m=>m.AllUsersModule)},
{ path:'userProfile',loadChildren:()=>import('./front-office/user-profile/user-profile.module').then(m=>m.UserProfileModule)},
];
export const AppConfig = [
  provideRouter(routes),
];