import {Component, OnInit} from '@angular/core';
import {Blog} from "../../models/blog";
import {BlogService} from "../../services/blog-service.service";
import {DatePipe, NgForOf, NgIf} from "@angular/common";
import {FormsModule} from "@angular/forms";
import {CommentaireService} from "../../services/commentaire-service.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-admin-blogs',
  standalone: true,
  imports: [
    NgForOf,
    DatePipe,
    FormsModule,
    NgIf
  ],
  templateUrl: './admin-blogs.component.html',
  styleUrl: './admin-blogs.component.css'
})
export class AdminBlogsComponent implements OnInit{
  blogs: Blog[] = [];
  commentsByBlogId: { [key: string]: any[] } = {};  // Store comments by blog ID
  newComment: { [key: string]: string } = {};  // Store new comment per blog

  constructor(
    private blogService: BlogService,
    private commentaireService: CommentaireService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.blogService.getAllBlogs().subscribe(
      (data) => {
        this.blogs = data.blogs;
        // Fetch comments for each blog
        this.blogs.forEach(blog => {
          this.commentaireService.getCommentairesByBlog(blog.id).subscribe(comments => {
            // Store comments by blogId
            this.commentsByBlogId[blog.id] = comments;
          });
        });
      },
      (error) => {
        console.error('Error fetching blogs:', error);
      }
    );
  }

  deleteBlog(blogId: string): void {
    this.blogService.deleteBlog(blogId).subscribe(
      () => {
        this.blogs = this.blogs.filter(blog => blog.id !== blogId);
        delete this.commentsByBlogId[blogId]; // Remove comments for deleted blog
      },
      (error) => {
        console.error('Error deleting blog:', error);
      }
    );
  }

  goToAddBlog(): void {
    this.router.navigate(['/addBlog']);
  }

  goToUpdateBlog(blogId: string): void {
    this.router.navigate(['/updateBlog', blogId]);
  }

  addComment(blogId: string): void {
    const commentaireData = {
      commentaire_content: this.newComment[blogId],
      blogId: blogId,
      commentaire_date: new Date().toISOString()
    };

    this.commentaireService.addCommentaire(blogId, commentaireData).subscribe(
      (response) => {
        // Clear the input field after posting
        this.newComment[blogId] = '';
        // Refresh comments for the blog
        this.commentaireService.getCommentairesByBlog(blogId).subscribe(comments => {
          this.commentsByBlogId[blogId] = comments;  // Update comments dynamically
        });
      },
      (error) => {
        console.error('Error posting comment:', error);
      }
    );
  }
}
