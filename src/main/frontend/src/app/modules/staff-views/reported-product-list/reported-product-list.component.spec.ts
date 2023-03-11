import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReportedProductListComponent } from './reported-product-list.component';

describe('ReportedProductListComponent', () => {
  let component: ReportedProductListComponent;
  let fixture: ComponentFixture<ReportedProductListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReportedProductListComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReportedProductListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
