import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ResponseViewComponent } from './response-view.component';

describe('ResponseViewComponent', () => {
  let component: ResponseViewComponent;
  let fixture: ComponentFixture<ResponseViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ResponseViewComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ResponseViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
