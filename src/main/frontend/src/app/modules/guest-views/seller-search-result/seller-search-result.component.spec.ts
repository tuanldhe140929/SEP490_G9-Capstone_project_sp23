import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SellerSearchResultComponent } from './seller-search-result.component';

describe('SellerSearchResultComponent', () => {
  let component: SellerSearchResultComponent;
  let fixture: ComponentFixture<SellerSearchResultComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SellerSearchResultComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SellerSearchResultComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
