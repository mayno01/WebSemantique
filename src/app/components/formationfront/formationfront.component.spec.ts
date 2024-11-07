import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormationfrontComponent } from './formationfront.component';

describe('FormationfrontComponent', () => {
  let component: FormationfrontComponent;
  let fixture: ComponentFixture<FormationfrontComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FormationfrontComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(FormationfrontComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
