import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EventParticipationComponent } from './event-participation.component';

describe('EventParticipationComponent', () => {
  let component: EventParticipationComponent;
  let fixture: ComponentFixture<EventParticipationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EventParticipationComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(EventParticipationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
