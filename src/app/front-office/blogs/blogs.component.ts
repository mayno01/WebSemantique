import {Component, OnInit} from '@angular/core';
import {Blog} from "../../models/blog";
import {BlogService} from "../../services/blog-service.service";
import {DatePipe, NgForOf, NgIf} from "@angular/common";
import {Router, RouterLink} from "@angular/router";
import {FormsModule} from "@angular/forms";
import {CommentaireService} from "../../services/commentaire-service.service";
import {Commentaire} from "../../models/commentaire";

@Component({
  selector: 'app-blogs',
  standalone: true,
  imports: [
    DatePipe,
    NgForOf,
    NgIf,
    FormsModule,
    RouterLink
  ],
  templateUrl: './blogs.component.html',
  styleUrl: './blogs.component.css'
})
export class BlogsComponent implements OnInit {
  blogs: Blog[] = [];
  commentsByBlogId: { [key: string]: Commentaire[] } = {}; // Stores comments by blog ID
  newComment: { [key: string]: string } = {}; // Stores new comments per blog

  constructor(
    private blogService: BlogService,
    private commentaireService: CommentaireService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.blogService.getAllBlogs().subscribe(
      (data) => {
        this.blogs = data.blogs;
        console.log('Fetched blogs:', this.blogs);
        this.blogs.forEach(blog => {
          this.loadComments(blog.id); // Load comments for each blog
        });
      },
      (error) => {
        console.error('Error fetching blogs:', error);
      }
    );
  }

  loadComments(blogId: string): void {
    this.commentaireService.getCommentairesByBlog(blogId).subscribe(
      (comments) => {
        if (comments && Array.isArray(comments)) {
          console.log(`Loaded comments for blog ${blogId}:`, comments);
          this.commentsByBlogId[blogId] = comments;
        } else {
          console.warn(`Unexpected data structure for comments in blog ${blogId}`, comments);
        }
      },
      (error) => {
        console.error(`Error loading comments for blog ${blogId}:`, error);
      }
    );
  }

  deleteBlog(blogId: string): void {
    this.blogService.deleteBlog(blogId).subscribe(
      () => {
        this.blogs = this.blogs.filter(blog => blog.id !== blogId);
        delete this.commentsByBlogId[blogId];
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
      () => {
        console.log(`Comment added for blog ${blogId}`);
        this.newComment[blogId] = ''; // Clear input
        this.loadComments(blogId); // Reload comments after adding
      },
      (error) => {
        console.error(`Error posting comment for blog ${blogId}:`, error);
      }
    );
  }
}
