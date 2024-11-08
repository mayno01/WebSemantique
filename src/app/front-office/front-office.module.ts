import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';


import { FrontOfficeRoutingModule } from './front-office-routing.module';


@NgModule({
  declarations: [

  ],
  imports: [
    CommonModule,
    FrontOfficeRoutingModule,
    HttpClientModule,
    FormsModule,
  ]
})
export class FrontOfficeModule { }
