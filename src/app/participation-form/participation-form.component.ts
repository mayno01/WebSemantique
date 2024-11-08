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
    // Initialize the form with only the necessary fields
    this.participationForm = this.fb.group({
      participantName: ['', Validators.required],
      eventId: ['', Validators.required],  // Event ID field (hidden from UI)
      participationType: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    // Fetch event data when the component is initialized
    this.fetchEventData();
  }

  fetchEventData(): void {
    // Simulate an API call or event data fetching (event name and other details are not needed for submission)
    const eventDataFromApi = { Description: 'A conference on the latest technology trends.', EventType: 'CONFERENCE', id: 'event-47096', Date: '2024-11-10', Name: 'Tech Conference' };

    // Store the event data for display, only set event ID in the form
    if (eventDataFromApi) {
      this.eventData = eventDataFromApi;  // Store the event data
      this.participationForm.get('eventId')?.setValue(this.eventData.id);  // Set the event ID in the form
    }
  }

  onSubmit() {
    // Check if the form is invalid, if so, return early
    if (this.participationForm.invalid) {
      return;
    }
  
    // Log form values for debugging
    const participationData = this.participationForm.value;
    console.log('Form Data:', participationData);

    // Call the participation service to add the participation, send only eventId and participantName
    const payload = {
      participantName: participationData.participantName,
      eventId: participationData.eventId,
      participationType: participationData.participationType
    };

    this.participationService.addParticipation(payload).subscribe(
      (response) => {
        // Handle successful response
        console.log('Response:', response);
        this.successMessage = 'Participation added successfully!';
        this.errorMessage = '';
        this.showSuccessMessage = true;  // Show success message
        this.participationForm.reset();  // Reset the form

        // Hide the success message after 3 seconds and navigate to events page
        setTimeout(() => {
          this.showSuccessMessage = false;
          this.router.navigate(['/events']);
        }, 3000);
      },
      (error) => {
        // Handle error response
        console.error('Error:', error);  // Log the error for debugging
        if (error.error && error.error.message) {
          this.errorMessage = error.error.message;  // Show the error message from the backend
        } else {
          this.errorMessage = 'Error adding participation!';  // Default error message
        }
        this.successMessage = '';
        this.showSuccessMessage = false;  // Hide success message on error
      }
    );
  }
}