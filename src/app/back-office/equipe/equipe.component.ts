import { Component, OnInit } from '@angular/core';
import { EquipeService } from '../../services/equipe.service';

@Component({
  selector: 'app-equipe',
  templateUrl: './equipe.component.html',
  styleUrls: ['./equipe.component.css']
})
export class EquipeComponent implements OnInit {
  // Update equipes to match the expected structure
  equipes: { id: string; type: string }[] = [];
  newEquipe: { Equipe_id: string; Equipe_type: string } = { Equipe_id: '', Equipe_type: '' };
  updateEquipeData: { Equipe_type: string } = { Equipe_type: '' };
  selectedEquipeId: string = '';

  constructor(private equipeService: EquipeService) {}

  ngOnInit(): void {
    this.getAllEquipes();
  }


  getAllEquipes(): void {
    this.equipeService.getAllEquipes().subscribe(
      (data: any) => {
        this.equipes = data; // Directly assign data if it's already an object
        console.log('equipessssssssss:', data)
      },
      (error) => {
        console.error('Error fetching equipes', error);
      }
    );
  }


  // Add a new equipe
  addEquipe(): void {
    this.equipeService.addEquipe(this.newEquipe).subscribe(
      (response: string) => {
        console.log(response);
        this.getAllEquipes(); // Refresh the list
        this.newEquipe = { Equipe_id: '', Equipe_type: '' }; // Clear input
      },
      (error) => {
        console.error('Error adding equipe', error);
      }
    );
  }

  // Update an existing equipe
  updateEquipe(): void {
    if (!this.selectedEquipeId) {
      console.error('No equipe selected for update');
      return;
    }

    this.equipeService.updateEquipe(this.selectedEquipeId, this.updateEquipeData).subscribe(
      (response: string) => {
        console.log(response);
        this.getAllEquipes(); // Refresh the list
        this.updateEquipeData = { Equipe_type: '' }; // Clear input
        this.selectedEquipeId = ''; // Reset selection
      },
      (error) => {
        console.error('Error updating equipe', error);
      }
    );
  }

  // Delete an equipe
  deleteEquipe(id: string): void {
    this.equipeService.deleteEquipe(id).subscribe(
      (response: string) => {
        console.log(response);
        this.getAllEquipes(); // Refresh the list
      },
      (error) => {
        console.error('Error deleting equipe', error);
      }
    );
  }
}
