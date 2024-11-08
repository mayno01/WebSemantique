import { Component, OnInit } from '@angular/core';
import { ReservationService } from '../../services/reservation.service';

@Component({
  selector: 'app-reservation',
  templateUrl: './reservation.component.html',
  styleUrls: ['./reservation.component.css']
})
export class ReservationComponent implements OnInit {
  reservations: { id: string; type: string; date: string; description: string; feedback: string }[] = [];
  newReservation = {
    Reservation_id: '',
    Reservation_type: '',
    Reservation_date: '',
    Reservation_description: '',
    Reservation_feedback: ''
  };
  updateReservationData = {
    Reservation_type: '',
    Reservation_date: '',
    Reservation_description: '',
    Reservation_feedback: ''
  };
  selectedReservationId: string = '';

  constructor(private reservationService: ReservationService) {}

  ngOnInit(): void {
    this.getAllReservations();
  }


  getAllReservations(): void {
    this.reservationService.getAllReservations().subscribe(
      (data: any) => {
        this.reservations = data; // Directly assign data if it's already an object
        console.log('reservationnnnnnnnn:', data)
      },
      (error) => {
        console.error('Error fetching reservation', error);
      }
    );
  }

  // Add a new reservation
  addReservation(): void {
    this.reservationService.addReservation(this.newReservation).subscribe(
      (response: string) => {
        console.log(response);
        this.getAllReservations(); // Refresh the list
        this.newReservation = {
          Reservation_id: '',
          Reservation_type: '',
          Reservation_date: '',
          Reservation_description: '',
          Reservation_feedback: ''
        }; // Clear input fields
      },
      (error) => {
        console.error('Error adding reservation', error);
      }
    );
  }

  // Update an existing reservation
  updateReservation(): void {
    if (!this.selectedReservationId) {
      console.error('No reservation selected for update');
      return;
    }

    this.reservationService.updateReservation(this.selectedReservationId, this.updateReservationData).subscribe(
      (response: string) => {
        console.log(response);
        this.getAllReservations(); // Refresh the list
        this.updateReservationData = {
          Reservation_type: '',
          Reservation_date: '',
          Reservation_description: '',
          Reservation_feedback: ''
        }; // Clear input fields
        this.selectedReservationId = ''; // Reset selection
      },
      (error) => {
        console.error('Error updating reservation', error);
      }
    );
  }

  // Delete a reservation
  deleteReservation(id: string): void {
    this.reservationService.deleteReservation(id).subscribe(
      (response: string) => {
        console.log(response);
        this.getAllReservations(); // Refresh the list
      },
      (error) => {
        console.error('Error deleting reservation', error);
      }
    );
  }
}
