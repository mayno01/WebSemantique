import { Component, OnInit } from '@angular/core';
import { Blog } from '../../models/blog';
import {ActivatedRoute, Router, RouterLink} from '@angular/router';
import { BlogService } from '../../services/blog-service.service';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import { DatePipe, NgForOf, NgIf } from '@angular/common';
import { BlogType } from '../../models/blog'; // Import BlogType enum

@Component({
    selector: 'app-update-blog',
    standalone: true,
  imports: [
    DatePipe,
    NgForOf,
    NgIf,
    ReactiveFormsModule,
    RouterLink
  ],
    templateUrl: './update-blog.component.html',
    styleUrls: ['./update-blog.component.css'] // Fixed typo
})
export class UpdateBlogComponent implements OnInit {
  blogForm: FormGroup;
  blogId!: string;
  blogTypes = Object.values(BlogType); // Use BlogType enum for dropdown options

  constructor(
    private blogService: BlogService,
    private route: ActivatedRoute,
    private router: Router,
    private fb: FormBuilder
  ) {
    this.blogForm = this.fb.group({
      title: ['', Validators.required],
      content: ['', Validators.required],
      type: ['', Validators.required],
      date: ['', Validators.required] // Add date field
    });
  }

  ngOnInit(): void {
    this.blogId = this.route.snapshot.paramMap.get('id')!;
    this.blogService.getBlogById(this.blogId).subscribe(
      (data: Blog) => {
        // Format the date if necessary
        const formattedDate = new Date(data.date).toISOString().split('T')[0]; // Format to YYYY-MM-DD
        this.blogForm.patchValue({ ...data, date: formattedDate });
      },
      (error) => console.error('Error fetching blog:', error)
    );
  }

  updateBlog(): void {
    if (this.blogForm.valid) {
      this.blogService.updateBlog(this.blogId, this.blogForm.value).subscribe(
        () => {
          this.router.navigate(['/blogs']);
        },
        (error) => console.error('Error updating blog:', error)
      );
    } else {
      console.error('Form is invalid');
    }
  }
}
