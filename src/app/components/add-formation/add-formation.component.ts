import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms'; // Import FormsModule here for ngModel
import { FormationService } from '../../services/formation.service';
import { Router } from '@angular/router';

// Define the FormationType enum outside of the class
enum FormationType {
  WORKSHOP = 'WORKSHOP',
  COURSE = 'COURSE'
}

@Component({
  selector: 'app-add-formation',
  templateUrl: './add-formation.component.html',
  styleUrls: ['./add-formation.component.css'],
  standalone: true,  // Mark component as standalone
  imports: [FormsModule] // Import FormsModule directly here
})
export class AddFormationComponent {
  // Define the formation object and initialize with default values
  formation = {
    name: '',
    description: '',
    formation_type: FormationType.COURSE // Default type
  };

  // Expose the enum to the template
  formationTypes = FormationType;

  constructor(private formationService: FormationService, private router: Router) {}

  // Define the addFormation method properly
  addFormation(): void {
    this.formationService.addFormation(this.formation).subscribe(
      () => {
        console.log('Formation added successfully');
        this.router.navigate(['/formations']);  // Navigate to formations list page
      },
      (error: any) => {
        console.error('Error adding formation:', error);
      }
    );
  }
  
}