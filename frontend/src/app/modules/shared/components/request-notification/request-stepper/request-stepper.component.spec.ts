import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RequestStepperComponent } from './request-stepper.component';

describe('RequestStepperComponent', () => {
  let component: RequestStepperComponent;
  let fixture: ComponentFixture<RequestStepperComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RequestStepperComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RequestStepperComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
