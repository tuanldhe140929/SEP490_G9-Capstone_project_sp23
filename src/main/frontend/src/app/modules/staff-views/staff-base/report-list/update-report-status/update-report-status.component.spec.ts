import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateReportStatusComponent } from './update-report-status.component';

describe('UpdateReportStatusComponent', () => {
  let component: UpdateReportStatusComponent;
  let fixture: ComponentFixture<UpdateReportStatusComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UpdateReportStatusComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UpdateReportStatusComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
