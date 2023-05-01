import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductApprovalErrorComponent } from './product-approval-error.component';

describe('ProductApprovalErrorComponent', () => {
  let component: ProductApprovalErrorComponent;
  let fixture: ComponentFixture<ProductApprovalErrorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ProductApprovalErrorComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProductApprovalErrorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
