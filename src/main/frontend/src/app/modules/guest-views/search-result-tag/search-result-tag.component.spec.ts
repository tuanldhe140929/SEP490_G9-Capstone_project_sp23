import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchResultTagComponent } from './search-result-tag.component';

describe('SearchResultTagComponent', () => {
  let component: SearchResultTagComponent;
  let fixture: ComponentFixture<SearchResultTagComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SearchResultTagComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SearchResultTagComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
