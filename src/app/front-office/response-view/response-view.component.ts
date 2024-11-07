import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ReclamationService } from '../../services/reclamation.service';

@Component({
  selector: 'app-response-view',
  templateUrl: './response-view.component.html',
  styleUrls: ['./response-view.component.css']
})
export class ResponseViewComponent implements OnInit {
  reclamationId!: string;
  responses: any[] = [];
  showAddResponseForm: boolean = false;
  newResponse = {
    responseText: '',
    date: '',
    type: 'MESSAGE' // Default type set to 'MESSAGE'
  };

  constructor(
    private route: ActivatedRoute,
    private reclamationService: ReclamationService,
  ) {}

  ngOnInit(): void {
    // Fetch the reclamationId from route params
    this.reclamationId = this.route.snapshot.paramMap.get('reclamationId')!;
    this.loadResponses(this.reclamationId);
  }

  loadResponses(reclamationId: string): void {
    // Call the service to get responses
    this.reclamationService.getResponsesForReclamation(reclamationId).subscribe({
      next: (data) => {
        this.responses = data;
      },
      error: (err) => {
        console.error('Error loading responses:', err);
      }
    });
  }

  addResponse(): void {
    const responseData = {
      reclamationId: this.reclamationId, // Include reclamationId
      responseText: this.newResponse.responseText,
      date: this.newResponse.date,
      type: this.newResponse.type,
    };

    this.reclamationService.addResponse(responseData).subscribe({
      next: () => {
        // Reload responses after adding new one
        this.loadResponses(this.reclamationId);
        this.showAddResponseForm = false; // Hide form on success
        this.newResponse = { responseText: '', date: '', type: 'MESSAGE' }; // Reset form
      },
      error: (err) => console.error('Error adding response:', err),
    });
  }

  deleteResponse(id: string): void {
    console.log('Deleting response with ID:', id); // Check if the ID is correct
    
    if (!id) {
      console.error('Response ID is missing');
      return;
    }
    
    if (confirm("Are you sure you want to delete this response?")) {
      this.reclamationService.deleteResponse(id).subscribe({
        next: () => {
          this.loadResponses(this.reclamationId); // Reload responses after deletion
        },
        error: (err) => {
          console.error('Error deleting response:', err);
        }
      });
    }
  }
  
}
