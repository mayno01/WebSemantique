import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { EventService } from '../../app/services/event.service';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-event-add',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './add-event.component.html',
  styleUrls: ['././add-event.component.html']
})
export class AddEventComponent implements OnInit {
  eventForm: FormGroup;

  // Enum for event types (similar to the ones you used in EventUpdateComponent)
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
    // You can add any initialization logic here if needed
  }

  // Submit the new event data to the backend
  addEvent(): void {
    if (this.eventForm.invalid) {
      return;  // Stop if the form is invalid
    }

    // Map the form data to match the backend expected keys
    const newEventData = {
      name: this.eventForm.value.eventName,
      description: this.eventForm.value.eventDescription,
      eventType: this.eventForm.value.eventType,
      date: this.eventForm.value.eventDate
    };

    // Send the new event data to the backend
    this.eventService.addEvent(newEventData).subscribe(
      (response) => {
        // Handle successful addition
        console.log('Event added successfully', response);
        this.router.navigate(['/event-dashboard']);  // Redirect to the event list page or wherever you want
      },
      (error) => {
        console.error('There was an error adding the event!', error);
      }
    );
  }
}
