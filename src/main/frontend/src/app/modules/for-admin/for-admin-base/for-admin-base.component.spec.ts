import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ForAdminBaseComponent } from './for-admin-base.component';

describe('ForAdminBaseComponent', () => {
  let component: ForAdminBaseComponent;
  let fixture: ComponentFixture<ForAdminBaseComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ForAdminBaseComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ForAdminBaseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
