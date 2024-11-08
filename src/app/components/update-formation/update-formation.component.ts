import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormationService } from '../../services/formation.service'; // Import FormationService
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common'; // Import CommonModule

@Component({
  selector: 'app-update-formation',
  templateUrl: './update-formation.component.html',
  styleUrls: ['./update-formation.component.css'],
  standalone: true,
  imports: [FormsModule, CommonModule] // Include CommonModule here
})
export class UpdateFormationComponent implements OnInit {
  formation: any; // Define formation type or interface if necessary

  constructor(
    private formationService: FormationService,
    private route: ActivatedRoute,
    public router: Router // Use 'public' or 'private' as needed
  ) {
    // Initialize formation with default values
    this.formation = {
      id: 0,
      name: '',
      description: '',
      formation_type: 'COURSE' // Default formation type
    };
  }

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id')); // Get formation ID from route
    this.loadFormation(id); // Load formation details
  }

  loadFormation(id: number): void {
    this.formationService.getFormation(id).subscribe(
      (formation) => {
        this.formation = formation; // Set the formation data
      },
      (error) => {
        console.error('Error fetching formation:', error);
      }
    );
  }

  updateFormation(): void {
    if (this.formation && this.formation.id) {
      this.formationService.updateFormation(this.formation.id, this.formation).subscribe(
        (response) => {
          console.log('Formation updated successfully:', response);
          this.router.navigate(['/formations']); // Navigate back to the formation list
        },
        (error) => {
          console.error('Error updating formation:', error);
        }
      );
    }
  }
}
