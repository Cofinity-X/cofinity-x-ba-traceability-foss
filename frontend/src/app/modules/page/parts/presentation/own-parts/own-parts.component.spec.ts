import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OwnPartsComponent } from './own-parts.component';

describe('OwnPartsComponent', () => {
  let component: OwnPartsComponent;
  let fixture: ComponentFixture<OwnPartsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OwnPartsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OwnPartsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
