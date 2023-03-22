import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReportedProductDetailsComponent } from './reported-product-details.component';

describe('ReportedProductDetailsComponent', () => {
  let component: ReportedProductDetailsComponent;
  let fixture: ComponentFixture<ReportedProductDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReportedProductDetailsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReportedProductDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
