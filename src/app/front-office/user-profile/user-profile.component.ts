import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-user-profile',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit {

  user: any = {}; // Assuming user data is an object
  errorMessage: string | null = null;

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.getUser();
  }

  getUser(): void {
    const userId = this.authService.getUserIdFromToken();
    if (userId) {
      this.authService.getUserById(userId).subscribe({
        next: (response: any) => {
          this.user = response;
        },
        error: (error: any) => {
          this.errorMessage = 'Failed to load user';
          console.error(error);
        }
      });
    } else {
      this.errorMessage = 'No user ID found in token';
    }
  }
}
