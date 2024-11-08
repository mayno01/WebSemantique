import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {DashboardComponent} from "./dashboard/dashboard.component";
import {AdminBlogsComponent} from "./admin-blogs/admin-blogs.component";

const routes: Routes = [

  { path: '', component: DashboardComponent },
  { path: 'blogs', component: AdminBlogsComponent },

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class BackOfficeRoutingModule { }
