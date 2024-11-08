import { Component, OnInit } from '@angular/core';
import { FormationService } from '../../services/formation.service';
import { InscriptionService } from '../../services/inscription.service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common'; // <-- Add this import

@Component({
  selector: 'app-add-inscription',
  templateUrl: './add-inscription.component.html',
  styleUrls: ['./add-inscription.component.css'],
  standalone: true,  // This tells Angular 18 that this component is standalone
  imports: [FormsModule, CommonModule],  // Add CommonModule here
})
export class AddInscriptionComponent implements OnInit {
  newInscription: any = {
    email: '',
    formationId: '',
    inscriptionType: ''
  };

  formations: any[] = [];  // Initialize formations array
  inscriptionTypes: string[] = ['MENSUEL', 'TRIMESTRIEL'];

  constructor(
    private formationService: FormationService,
    private inscriptionService: InscriptionService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.formationService.getAllFormations().subscribe(
      (response) => {
        // Assuming response contains an array of formations under 'Formations'
        this.formations = response.Formations || [];
      },
      (error) => {
        console.error('Error fetching formations:', error);
      }
    );
  }

  addInscription(): void {
    console.log('Inscription type:', this.newInscription.inscriptionType);  // Log to see the value
    this.inscriptionService.addInscription(this.newInscription).subscribe(
      (response) => {
        console.log('Inscription added:', response);
        this.router.navigate(['/inscriptions']);
      },
      (error) => {
        console.error('Error adding inscription:', error);
        if (error.status === 400 && error.error === 'Invalid inscription type provided.') {
          alert('Please select a valid inscription type (mensuel or trimestriel).');
        } else {
          alert('inscrit.');
        }
      }
    );
  }

  cancel(): void {
    this.router.navigate(['/inscriptions']);
  }
}
