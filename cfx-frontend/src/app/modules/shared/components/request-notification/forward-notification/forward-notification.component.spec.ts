import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ForwardNotificationComponent } from './forward-notification.component';

describe('ForwardNotificationComponent', () => {
  let component: ForwardNotificationComponent;
  let fixture: ComponentFixture<ForwardNotificationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ForwardNotificationComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ForwardNotificationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
