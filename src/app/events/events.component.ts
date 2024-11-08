// Import the necessary Angular modules
import { Component, OnInit } from '@angular/core';
import { EventService } from '../../app/services/event.service';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router'; // <-- Import the Router service

@Component({
  selector: 'app-events',
  templateUrl: './events.component.html',
  standalone: true,
  imports: [ CommonModule ],
  styleUrls: ['./events.component.css']
})
export class EventsComponent implements OnInit {
  events: any[] = [];  // Array to hold event data
  errorMessage: string = '';  // To hold any error messages

  constructor(private router: Router, private eventService: EventService) {}

  ngOnInit(): void {
    this.getAllEvents();
  }

  // Fetch events from the event service
  getAllEvents(): void {
    this.eventService.getAllEvents().subscribe(
      (response) => {
        this.events = response.Events;  // Access the 'Events' array inside the response object
      },
      (error) => {
        console.error('There was an error retrieving events!', error);
        this.errorMessage = 'There was an issue fetching the events. Please try again later.';  // Show an error message
      }
    );
  }

  // Navigate to the details page of the selected event
  navigateToEventDetails(eventId: string): void {
    this.router.navigate(['/event', eventId]);
  }
}
