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
  selector: 'app-formation',
  templateUrl: './formation.component.html',
  styleUrls: ['./formation.component.css'],
  standalone: true,  // This tells Angular 18 that this component is standalone
  imports: [CommonModule],  // Import CommonModule here
})
export class FormationComponent implements OnInit {
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

  
  addFormation(): void {
    this.router.navigate(['/addformation']); // Navigate to add hotel page
  }
  updateFormation(id: number): void { // Accept id parameter
    this.router.navigate(['/updateformation', id]); // Navigate to edit hotel page
  }
  

  deleteFormation(id: string): void {
    this.formationService.deleteFormation(id).subscribe(
      () => {
        console.log('Formation deleted successfully');
        this.getFormations(); // Refresh the list
      },
      (error: HttpErrorResponse) => {
        console.error('Error deleting formation:', error.message);
      }
    );
  }
}
