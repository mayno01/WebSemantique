import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { FrontOfficeRoutingModule } from './front-office-routing.module';
import { HttpClientModule } from '@angular/common/http';
import { ReclamationService } from '../services/reclamation.service';
import { ReclamationsComponent } from './reclamations/reclamations.component';
import { ResponseViewComponent } from './response-view/response-view.component';
import { FormsModule } from '@angular/forms'; 
import { AddReclamationComponent } from './add-reclamation/add-reclamation.component';



@NgModule({
  declarations: [
     ReclamationsComponent,
     ResponseViewComponent,
     AddReclamationComponent

  ],
  imports: [
    CommonModule,
    FrontOfficeRoutingModule,
    HttpClientModule,
    FormsModule,

  ],
  providers: [ReclamationService]

})
export class FrontOfficeModule { }
