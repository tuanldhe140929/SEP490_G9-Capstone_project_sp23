import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateStaffStatusComponent } from './update-staff-status.component';

describe('UpdateStaffStatusComponent', () => {
  let component: UpdateStaffStatusComponent;
  let fixture: ComponentFixture<UpdateStaffStatusComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UpdateStaffStatusComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UpdateStaffStatusComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
