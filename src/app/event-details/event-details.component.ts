import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';  // Import Router

import { EventService } from '../../app/services/event.service';
import { CommonModule } from '@angular/common'; 
import { ParticipationService } from '../../app/services/participation.service'; // Add the ParticipationService

@Component({
  selector: 'app-event-details',
  templateUrl: './event-details.component.html',
  standalone: true,
  imports: [ CommonModule],
  styleUrls: ['./event-details.component.css'],
})
export class EventDetailsComponent implements OnInit {
  event: any;
  userId: string = 'user123';  // Replace with the actual user ID (from auth service or cookie)

  constructor(
    private eventService: EventService,
    private participationService: ParticipationService, // Inject the ParticipationService
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    const eventId = this.route.snapshot.paramMap.get('id');
    console.log('Event ID:', eventId); // Log the event ID
    if (eventId) {
      this.eventService.getEventById(eventId).subscribe(
        (data) => {
          this.event = data;
          console.log('Event Data:', this.event); // Log the event data
        },
        (error) => {
          console.error('Error fetching event details', error);
        }
      );
    }
  }

  // Method to participate in the event
  onParticipateClick() {
    this.router.navigate(['/participation-form']);
  }

  // Method to navigate back to the events list
  goBack(): void {
    this.router.navigate(['/events']);  // Navigate to the events list
  }
}
