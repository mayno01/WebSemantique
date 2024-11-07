import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ReclamationService } from '../../services/reclamation.service';

@Component({
  selector: 'app-add-reclamation',
  templateUrl: './add-reclamation.component.html',
  styleUrls: ['./add-reclamation.component.css']
})
export class AddReclamationComponent {
  newReclamation = {
    title: '',
    description: '',
    date: '',
    type: ''
  };
  
  reclamationTypes = ['ERREURS_DE_COMMANDE', 'PROBLEMES_DE_QUALITE_DES_PLANTES', 'PROBLEMES_LIES_AUX_PROMOTIONS', 'PRODUITS_DE_FECTUEUX', 'SERVICE_CLIENTELE_INSATISFAISANT']; // Replace with your actual types
  addMessage: string = '';

  constructor(private reclamationService: ReclamationService, private router: Router) {}

  submitReclamation(): void {
    this.reclamationService.addReclamation(this.newReclamation).subscribe({
      next: (response) => {
        this.addMessage = response;
        this.clearForm();
        // Optionally navigate back to the reclamation list
        this.router.navigate(['/reclamations']);
      },
      // error: (err) => {
      //   this.addMessage = 'Error adding reclamation';
      //   console.error(err);
      // }
    });
  }

  clearForm(): void {
    this.newReclamation = { title: '', description: '', date: '', type: '' };
  }
}
