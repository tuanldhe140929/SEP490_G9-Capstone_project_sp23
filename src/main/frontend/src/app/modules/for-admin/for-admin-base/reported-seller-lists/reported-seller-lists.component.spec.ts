import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReportedSellerListsComponent } from './reported-seller-lists.component';

describe('ReportedSellerListsComponent', () => {
  let component: ReportedSellerListsComponent;
  let fixture: ComponentFixture<ReportedSellerListsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReportedSellerListsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReportedSellerListsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
