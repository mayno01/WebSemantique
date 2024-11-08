import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EquipeComponent  } from './equipe/equipe.component';
import { ReservationComponent  } from './reservation/reservation.component';
import { BackOfficeRoutingModule } from './back-office-routing.module';
import {FormsModule} from "@angular/forms";


@NgModule({
  declarations: [
    EquipeComponent,
    ReservationComponent
  ],
  imports: [
    CommonModule,
    BackOfficeRoutingModule,
    FormsModule
  ]
})
export class BackOfficeModule { }
