// back-office.module.ts
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { BackOfficeRoutingModule } from './back-office-routing.module';
import { AllReclamationComponent } from './all-reclamation/all-reclamation.component';

@NgModule({
  declarations: [
    AllReclamationComponent
  ],
  imports: [
    CommonModule,
    BackOfficeRoutingModule,
    HttpClientModule  // Ensure HttpClientModule is imported
  ]
})
export class BackOfficeModule {}
