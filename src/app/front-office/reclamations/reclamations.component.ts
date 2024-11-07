// src/app/reclamation/reclamation.component.ts

import { Component, OnInit } from '@angular/core';
import { ReclamationService } from '../../services/reclamation.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-reclamation',
  templateUrl: './reclamations.component.html',
  styleUrls: ['./reclamations.component.css']
})
export class ReclamationsComponent implements OnInit {
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
  viewResponse(reclamationId: string): void {
    this.router.navigate([`/reclamations/${reclamationId}/responses`]);
  }
  deleteReclamation(reclamationId: string): void {
    if (confirm('Are you sure you want to delete this reclamation?')) {
      this.reclamationService.deleteReclamation(reclamationId).subscribe({
        next: () => {
          this.reclamations = this.reclamations.filter(
            (reclamation) => reclamation.id !== reclamationId
          );
          alert('Reclamation deleted successfully');
        },
        error: (err) => {
          this.errorMessage = 'Error deleting reclamation';
          console.error(err);
        },
      });
    }
  }
  updateReclamation(reclamationId: string): void {
    this.router.navigate([`/reclamations/update/${reclamationId}`]); // Navigate to update page with reclamationId
  }

  addReclamation(): void {
    this.router.navigate(['/reclamations/add']); // Navigate to add new reclamation page
  }
}
