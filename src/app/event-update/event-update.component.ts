import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { EventService } from '../../app/services/event.service';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-event-update',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './event-update.component.html',
  styleUrls: ['./event-update.component.css']
})
export class EventUpdateComponent implements OnInit {
  eventId!: string;
  eventForm: FormGroup;
  eventData: any = {};  // Store event data

  // Enum for event types (better use a TypeScript enum for type safety)
  eventTypes = [
    'COMPAGNE_DE_SENSIBILISATION',
    'CONFERENCE',
    'FORUM',
    'JOURNEE_PORTE_OUVRTE',
    'SEMINAIRE'
  ];

  constructor(
    private eventService: EventService,
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router
  ) {
    // Initialize the form group with default values and validators
    this.eventForm = this.fb.group({
      eventName: ['', Validators.required],  // Event name
      eventDescription: ['', Validators.required],  // Event description
      eventType: ['', Validators.required],  // Event type
      eventDate: ['', Validators.required]  // Event date
    });
  }

  ngOnInit(): void {
    // Get the event ID from the route parameters
    this.eventId = this.route.snapshot.paramMap.get('id')!;
    this.getEventDetails();
  }

  // Fetch event details from the backend service
  getEventDetails(): void {
    this.eventService.getEventById(this.eventId).subscribe(
      (response) => {
        this.eventData = response;
        this.setFormValues();  // Populate the form with event data
      },
      (error) => {
        console.error('There was an error fetching the event details!', error);
      }
    );
  }

  // Set form values based on the fetched event data
  setFormValues(): void {
    if (this.eventData) {
      this.eventForm.setValue({
        eventName: this.eventData.Name || '',
        eventDescription: this.eventData.Description || '',
        eventType: this.eventData.EventType || '',
        eventDate: this.eventData.Date || ''
      });
    }
  }

  // Submit the updated event data to the backend
  updateEvent(): void {
    if (this.eventForm.invalid) {
      return;  // Stop if the form is invalid
    }

    // Map the form data to match the backend expected keys
    const updatedEventData = {
      name: this.eventForm.value.eventName,
      description: this.eventForm.value.eventDescription,
      eventType: this.eventForm.value.eventType,
      date: this.eventForm.value.eventDate
    };

    // Send the updated event data to the backend
    this.eventService.updateEvent(this.eventId, updatedEventData).subscribe(
      (response) => {
        // Handle successful update (response is a plain text string)
        console.log('Event updated successfully', response);
        this.router.navigate(['/event-dashboard']);  // Redirect to the event list page
      },
      (error) => {
        console.error('There was an error updating the event!', error);
      }
    );
  }
}
