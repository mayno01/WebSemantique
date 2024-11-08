import { Component, OnInit } from '@angular/core';

import { ParticipationService } from '../services/participation.service';
import { EventService } from '../services/event.service';  // Import EventService
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-event-participation',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './event-participation.component.html',
  styleUrls: ['./event-participation.component.css'],
})
export class EventParticipationComponent implements OnInit {
  eventId: string = '';
  eventName: string = 'Unknown Event';
  participations: any[] = [];
  errorMessage: string = '';
  successMessage: string = '';  // Add successMessage to handle successful deletion feedback

  constructor(
    private route: ActivatedRoute,
    private router:Router ,
    private participationService: ParticipationService,
    private eventService: EventService  // Inject EventService to fetch event name
  ) {}

  ngOnInit() {
    this.route.params.subscribe((params) => {
      this.eventId = params['eventId'];
      if (this.eventId) {
        this.fetchEventDetails();  // Fetch event details for name
        this.fetchParticipations();  // Fetch participations when eventId is available
      }
    });
  }

  fetchEventDetails() {
    // Fetch the event name using the event ID
    this.eventService.getEventById(this.eventId).subscribe({
      next: (data) => {
        this.eventName = data.Name || 'Unknown Event';  // Extract event name
        console.log('Event API Response:', data);  // Log for debugging
      },
      error: (error) => {
        console.error('Error fetching event details', error);
        this.eventName = 'Unknown Event';
      },
    });
  }

  fetchParticipations() {
    console.log('Fetching participations for event ID:', this.eventId);

    if (this.eventId && this.eventId.trim()) {
      this.participationService.getParticipationByEvent(this.eventId).subscribe({
        next: (response) => {
          console.log('API Response:', response);
          this.participations = response.Participations || [];
          this.errorMessage = '';  // Reset error message if valid
        },
        error: (error) => {
          console.log('Error:', error);
          this.errorMessage = 'Invalid event ID.';
          this.participations = [];
        },
      });
    } else {
      this.errorMessage = 'Please enter a valid event ID.';
      this.participations = [];
    }
  }

  deleteParticipation(participationId: string) {
    // Show a confirmation dialog
    const confirmed = window.confirm('Are you sure you want to delete this participant?');
    
    if (confirmed) {
      this.participationService.deleteParticipation(participationId).subscribe({
        next: (response: string) => {
          this.successMessage = response;  // Set the success message
          this.errorMessage = '';  // Clear any previous error messages
          this.fetchParticipations();  // Refresh the list after deletion
        },
        error: (error) => {
          console.error('Error deleting participant:', error);
          this.errorMessage = 'Failed to delete participant. Please try again.';
          this.successMessage = '';  // Clear success message on error
        },
      });
    }
  }
  goBack() {
    
    this.router.navigate(['/event-dashboard']);
  }
}
