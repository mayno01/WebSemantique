
import { provideRouter, Routes } from '@angular/router';
import { EventsComponent } from './events/events.component';
import { EventDetailsComponent } from './event-details/event-details.component';
import { ParticipationFormComponent } from './participation-form/participation-form.component';
import { EventDashboardComponent } from './event-dashboard/event-dashboard.component';
import { EventUpdateComponent } from './event-update/event-update.component';
import { AddEventComponent } from './add-event/add-event.component';
import { EventParticipationComponent } from './event-participation/event-participation.component';
export const routes: Routes = [
 
  { path: 'register', loadChildren: () => import('./register/register.module').then(m => m.RegisterModule) },
  { path: 'home', loadChildren: () => import('./front-office/front-office.module').then(m => m.FrontOfficeModule) },
  { path: 'admin', loadChildren: () => import('./back-office/back-office.module').then(m => m.BackOfficeModule) },
  { path: 'login', loadChildren: () => import('./login/login.module').then(m => m.LoginModule) },
  { path: 'event/:id', component: EventDetailsComponent },
  { path: 'events', component: EventsComponent },
  { path: 'participation-form', component: ParticipationFormComponent } ,
  { path: 'event-dashboard', component: EventDashboardComponent }, 
  { path: 'event/update/:id', component: EventUpdateComponent }, 
  { path: 'add-event', component: AddEventComponent },  
  { path: 'event-participation/:eventId', component: EventParticipationComponent }
];

export const AppConfig = [
  provideRouter(routes),
];