import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ParticipationService } from '../services/participation.service';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-participation-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './participation-form.component.html',
  styleUrls: ['./participation-form.component.css']
})
export class ParticipationFormComponent implements OnInit {
  participationForm: FormGroup;
  successMessage: string = '';
  errorMessage: string = '';
  showSuccessMessage: boolean = false;  // Flag for controlling success message visibility
  eventData: any;  // Store the event data

  constructor(
    private participationService: ParticipationService,
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router  // Inject Router for navigation
  ) {
    this.participationForm = this.fb.group({
      participantName: ['', Validators.required],
      eventId: ['', Validators.required],  // Event ID field (hidden from UI)
      eventName: ['', Validators.required], // Event name to display
      participationType: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    // Simulate getting event data (e.g., from a service or route)
    this.fetchEventData();
  }

  fetchEventData(): void {
    // Simulate an API call or event data fetching
    const eventDataFromApi = { Description: 'A conference on the latest technology trends.', EventType: 'CONFERENCE', id: '1', Date: '2024-11-10', Name: 'Tech Conference' };

    // Set the event data (ID and name) in the form after retrieving it
    if (eventDataFromApi) {
      this.eventData = eventDataFromApi;  // Store the event data
      this.participationForm.get('eventId')?.setValue(this.eventData.id);  // Set the event ID in the form
      this.participationForm.get('eventName')?.setValue(this.eventData.Name);  // Set the event name in the form
    }
  }

  onSubmit() {
    if (this.participationForm.invalid) {
      return;
    }
  
    const participationData = this.participationForm.value;
  
    this.participationService.addParticipation(participationData).subscribe(
      (response) => {
        console.log('Response:', response); // Add this for debugging
        this.successMessage = 'Participation added successfully!';
        this.errorMessage = '';
        this.showSuccessMessage = true;  // Show the success message
        this.participationForm.reset();

        // Hide the success message after 3 seconds and redirect
        setTimeout(() => {
          this.showSuccessMessage = false;
          this.router.navigate(['/events']);  // Redirect to the events page
        }, 3000);
      },
      (error) => {
        console.error('Error:', error);  // Add this for debugging
        this.errorMessage = 'Error adding participation!';
        this.successMessage = '';
        this.showSuccessMessage = false;  // Hide success message in case of error
      }
    );
  }
}
