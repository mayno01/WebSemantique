import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { FormationService } from '../../services/formation.service';
import { CommonModule } from '@angular/common';  // Import CommonModule
import { Router } from '@angular/router';
interface Formation {
  id: string;  // Added 'id' to uniquely identify formations
  name: string;
  description: string;
  formation_type: string;
}

@Component({
  selector: 'app-formationfront',
  
  templateUrl: './formationfront.component.html',
  styleUrl: './formationfront.component.css',
  standalone: true,  // This tells Angular 18 that this component is standalone
  imports: [CommonModule],  // Import CommonModule here
})

export class FormationfrontComponent implements OnInit{
  formations: Formation[] = [];

  constructor(private formationService: FormationService, private router: Router) {}

  ngOnInit(): void {
    this.getFormations();
  }

  getFormations(): void {
    this.formationService.getAllFormations().subscribe(
      (data: { Formations: Formation[] }) => {
        this.formations = data.Formations;  // Assuming the backend returns a 'Formations' array
      },
      (error: HttpErrorResponse) => {
        console.error('Error fetching formations:', error.message);
      }
    );
  }

  navigateToAddInscription() {
    this.router.navigate(['/addinscription']);
  }

}
