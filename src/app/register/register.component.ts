import { Component } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';


@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule,CommonModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent {
  username: string = '';
  password: string = '';
  role: string = 'SIMPLE_USER'; 

  constructor(private authService: AuthService ,private router: Router) {}

  register() {
    this.authService.register(this.username, this.password, this.role).subscribe(
      response => {
        console.log('User registered successfully', response);
       
        this.router.navigate(['/login']);
      },
      error => {
        console.error('Error registering user', error);
      }
    );
  }
}
