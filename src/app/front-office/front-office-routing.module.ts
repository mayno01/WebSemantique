import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {HomeComponent} from "./home/home.component";
import {AddBlogComponent} from "./add-blog/add-blog.component";
import {BlogsComponent} from "./blogs/blogs.component";
import {UpdateBlogComponent} from "./update-blog/update-blog.component";

const routes: Routes = [

  { path: '', component: HomeComponent },
  { path: 'addBlog', component: AddBlogComponent },
  { path: 'blogs', component: BlogsComponent },
  { path: 'updateBlog/:id', component: UpdateBlogComponent },  // Add the route with the ID

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class FrontOfficeRoutingModule { }
