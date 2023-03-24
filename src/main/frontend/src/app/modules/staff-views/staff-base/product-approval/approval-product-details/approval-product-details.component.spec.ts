import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApprovalProductDetailsComponent } from './approval-product-details.component';

describe('ApprovalProductDetailsComponent', () => {
  let component: ApprovalProductDetailsComponent;
  let fixture: ComponentFixture<ApprovalProductDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ApprovalProductDetailsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ApprovalProductDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
