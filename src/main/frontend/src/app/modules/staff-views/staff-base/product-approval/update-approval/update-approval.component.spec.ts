import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateApprovalComponent } from './update-approval.component';

describe('UpdateApprovalComponent', () => {
  let component: UpdateApprovalComponent;
  let fixture: ComponentFixture<UpdateApprovalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UpdateApprovalComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UpdateApprovalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
