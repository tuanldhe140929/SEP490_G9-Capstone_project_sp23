import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApprovalDownloadComponent } from './approval-download.component';

describe('ApprovalDownloadComponent', () => {
  let component: ApprovalDownloadComponent;
  let fixture: ComponentFixture<ApprovalDownloadComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ApprovalDownloadComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ApprovalDownloadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
