import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReportedProductDownloadComponent } from './reported-product-download.component';

describe('ReportedProductDownloadComponent', () => {
  let component: ReportedProductDownloadComponent;
  let fixture: ComponentFixture<ReportedProductDownloadComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReportedProductDownloadComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReportedProductDownloadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
