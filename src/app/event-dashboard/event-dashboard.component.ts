import { Component, OnInit } from '@angular/core';
import { EventService } from '../../app/services/event.service';  // Import EventService
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router'; // Import Router for navigation
import { ChangeDetectorRef } from '@angular/core'; // Import ChangeDetectorRef

@Component({
  selector: 'app-event-dashboard', // Component selector
  templateUrl: './event-dashboard.component.html', // Template URL
  standalone: true,
  imports: [CommonModule], // Importing CommonModule
  styleUrls: ['./event-dashboard.component.css'] // Styles URL
})
export class EventDashboardComponent implements OnInit {
  events: any[] = [];  // Array to hold event data
  errorMessage: string = '';  // To hold any error messages

  constructor(
    private router: Router,
    private eventService: EventService,
    private cdr: ChangeDetectorRef // Inject ChangeDetectorRef for manual change detection
  ) {}

  ngOnInit(): void {
    this.getAllEvents();
  }

  // Fetch events from the EventService
  getAllEvents(): void {
    this.eventService.getAllEvents().subscribe(
      (response) => {
        this.events = response.Events;  // Assuming response contains an array of events
      },
      (error) => {
        console.error('There was an error retrieving events!', error);
        this.errorMessage = 'There was an issue fetching the events. Please try again later.';  // Show error message
      }
    );
  }

  // Navigate to the details page of the selected event
  navigateParticipants(eventId: string): void {
    this.router.navigate(['/event-participation', eventId]);  // Navigate to the event details page
  }

  // Delete an event
  deleteEvent(eventId: string): void {
    if (confirm('Are you sure you want to delete this event?')) {
      this.eventService.deleteEvent(eventId).subscribe(
        (response) => {
          console.log('Event deleted successfully', response);
          // Remove the deleted event from the local array instead of refetching
          this.events = this.events.filter(event => event.id !== eventId);
          this.cdr.detectChanges(); // Manually trigger change detection if needed
        },
        (error) => {
          console.error('There was an error deleting the event!', error);
          this.errorMessage = 'There was an issue deleting the event. Please try again later.';
        }
      );
    }
  }

  // Update an event
  updateEvent(eventId: string): void {
    this.router.navigate(['/event/update', eventId]);  // Example route to event update page
  }

  // Navigate to the add event page
  navigateToAddEvent(): void {
    this.router.navigate(['/add-event']);
  }
}
