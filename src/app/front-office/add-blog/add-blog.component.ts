import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { Blog, BlogType } from "../../models/blog";
import { BlogService } from "../../services/blog-service.service";
import {Router, RouterLink} from "@angular/router";
import { CommonModule } from "@angular/common"; // Import CommonModule here

@Component({
  selector: 'app-add-blog',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    CommonModule,
    RouterLink,
    // Add CommonModule here
  ],
  templateUrl: './add-blog.component.html',
  styleUrl: './add-blog.component.css'
})
export class AddBlogComponent {
  blogForm: FormGroup;
  blogTypes = Object.values(BlogType); // ['Blague', 'Question', 'Conseil', 'Recommandation']

  constructor(
    private fb: FormBuilder,
    private blogService: BlogService,
    private router: Router
  ) {
    this.blogForm = this.fb.group({
      title: ['', Validators.required],
      type: ['', Validators.required],
      content: ['', Validators.required],
      date: ['', Validators.required],
    });
  }

  ngOnInit(): void {}

  onSubmit(): void {
    if (this.blogForm.valid) {
      const newBlog: Blog = this.blogForm.value;
      this.blogService.addBlog(newBlog).subscribe({
        next: () => {
          alert('Blog added successfully!');
          this.router.navigate(['/blogs']); // Navigate to the blog list or another page after adding
        },
        error: (err) => {
          console.error('Error adding blog:', err);
          alert('Failed to add the blog. Please try again.');
        }
      });
    }
  }
}
