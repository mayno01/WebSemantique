import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ReclamationService } from '../../services/reclamation.service'; 
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-all-reclamation',
  standalone: true,
  imports: [FormsModule,CommonModule],
  templateUrl: './all-reclamation.component.html',
  styleUrls: ['./all-reclamation.component.css']  // Corrected this line
})
export class AllReclamationComponent  implements OnInit {
  reclamations: any[] = []; // Array to hold the list of reclamations
  errorMessage: string = '';
  responses: any[] = [];     // Array to hold the list of responses for a reclamation

  constructor(private reclamationService: ReclamationService, private router: Router) {}

  ngOnInit(): void {
    this.loadReclamations();
  }
  loadReclamations(): void {
    this.reclamationService.getReclamations().subscribe({
      next: (data) => {
        this.reclamations = data.Reclamations; // Ensure this matches the backend response
        console.log(this.reclamations); // Check if data is assigned properly
      },
      error: (err) => {
        this.errorMessage = 'Error loading reclamations';
        console.error(err);
      },
    });
  }

 
 

}
