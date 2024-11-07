import { Component ,OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common'; 
@Component({
  selector: 'app-all-users',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './all-users.component.html',
  styleUrls: ['./all-users.component.css']
})
export class AllUsersComponent implements OnInit {
  users: any[] = []; 
  errorMessage: string | null = null; 

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    this.getUsers();
  }

  getUsers(): void {
    this.authService.getAllUsers().subscribe(
      (response: any) => {
        this.users = response; 
      },
      (error: any) => {
        this.errorMessage = 'Failed to load users';
        console.error(error);
      }
    );
  }
  Logout() {
    this.authService.removeToken(); 
    this.router.navigate(['/login']);  
  }
}