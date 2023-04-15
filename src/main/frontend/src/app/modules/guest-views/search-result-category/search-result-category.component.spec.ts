import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchResultCategoryComponent } from './search-result-category.component';

describe('SearchResultCategoryComponent', () => {
  let component: SearchResultCategoryComponent;
  let fixture: ComponentFixture<SearchResultCategoryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SearchResultCategoryComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SearchResultCategoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
