import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StaffBaseComponent } from './staff-base.component';

describe('StaffBaseComponent', () => {
  let component: StaffBaseComponent;
  let fixture: ComponentFixture<StaffBaseComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StaffBaseComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(StaffBaseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
