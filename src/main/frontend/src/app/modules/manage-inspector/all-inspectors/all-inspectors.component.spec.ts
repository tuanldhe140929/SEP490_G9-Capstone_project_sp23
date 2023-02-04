import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AllInspectorsComponent } from './all-inspectors.component';

describe('AllInspectorsComponent', () => {
  let component: AllInspectorsComponent;
  let fixture: ComponentFixture<AllInspectorsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AllInspectorsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AllInspectorsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
