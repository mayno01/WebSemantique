import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup } from '@angular/forms';
import { CommonModule } from '@angular/common'; 
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-edit-user',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './edit-user.component.html',
  styleUrls: ['./edit-user.component.css']
})
export class EditUserComponent implements OnInit {
  editForm!: FormGroup;
  userId!: string;
  groupTypes = ['VIP', 'PREMIUM', 'BASIC']; 
  groups = [
    { id: '334658713312', name: 'VIP' },
    { id: '415103205182', name: 'PREMIUM' },
    { id: '152033818458', name: 'BASIC' }


  ];

  constructor(
    private authService: AuthService,
    private route: ActivatedRoute,
    private fb: FormBuilder,
    private router: Router
  ) {}

  ngOnInit(): void {
    // Retrieve the userId from the route parameters
    this.userId = this.route.snapshot.paramMap.get('id')!;
    
    // Initialize the form
    this.editForm = this.fb.group({
      username: [{ value: '', disabled: true }],
      role: [{ value: '', disabled: true }],
      group: ['']
    });

    // Load the user data into the form
    this.authService.getUserById(this.userId).subscribe((user) => {
      this.editForm.patchValue({
        username: user.username,
        role: user.role,
        group: user.groupName // Display current group name in the form
      });
    });
  }
  Logout() {
    this.authService.removeToken(); 
    this.router.navigate(['/login']);  
  }
  onSubmit(): void {
    const selectedGroupName = this.editForm.value.group;
    
   
    const selectedGroup = this.groups.find(group => group.name === selectedGroupName);
    
    if (selectedGroup) {
      const updatedGroupId = selectedGroup.id;
      this.authService.updateUserGroup(this.userId, updatedGroupId).subscribe(
        (response) => {
          console.log('Group updated successfully:', response);
          this.router.navigate(['/allusers']); 
        },
        (error) => {
          console.error('Error updating group:', error);
        }
      );
    } else {
      console.error('Invalid group selected');
    }
  }
}
