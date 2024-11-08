import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { InscriptionService } from '../../services/inscription.service';
import { CommonModule } from '@angular/common'; // Import CommonModule
import { Router } from '@angular/router';

interface Inscription {
  email: string;
  formationId: string;
  inscriptionType: string;
}

@Component({
  selector: 'app-inscription',
  templateUrl: './inscription.component.html',
  styleUrls: ['./inscription.component.css'],
  standalone: true, // This tells Angular 18 that this component is standalone
  imports: [CommonModule], // Import CommonModule here
})
export class InscriptionComponent implements OnInit {
  inscriptions: Inscription[] = [];

  constructor(private inscriptionService: InscriptionService, private router: Router) {}

  ngOnInit(): void {
    this.getInscriptions(); // Fetch inscriptions on component load
  }

  // Method to get all inscriptions
  getInscriptions(): void {
    this.inscriptionService.getAllInscriptions().subscribe(
      (data: Inscription[]) => {
        this.inscriptions = data; // Set inscriptions to the fetched data
      },
      (error: HttpErrorResponse) => {
        console.error('Error fetching inscriptions:', error.message); // Handle error
      }
    );
  }

  // Method to navigate to add inscription page
  addInscription(): void {
    this.router.navigate(['/addinscription']); // Navigate to add inscription page
  }

  // Method to delete inscription by email and formation ID
  deleteInscription(email: string, formationId: string): void {
    this.inscriptionService.deleteInscription(email, formationId).subscribe(
      () => {
        console.log('Inscription deleted successfully');
        this.getInscriptions(); // Refresh the list after deletion
      },
      (error: HttpErrorResponse) => {
        console.error('Error deleting inscription:', error.message); // Handle error
      }
    );
  }
}
