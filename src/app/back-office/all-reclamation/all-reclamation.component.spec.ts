import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AllReclamationComponent } from './all-reclamation.component';

describe('AllReclamationComponent', () => {
  let component: AllReclamationComponent;
  let fixture: ComponentFixture<AllReclamationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AllReclamationComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AllReclamationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
