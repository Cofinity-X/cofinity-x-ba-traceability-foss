import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RequestPartSelectionComponent } from './request-part-selection.component';

describe('RequestPartSelectionComponent', () => {
  let component: RequestPartSelectionComponent;
  let fixture: ComponentFixture<RequestPartSelectionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RequestPartSelectionComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RequestPartSelectionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
