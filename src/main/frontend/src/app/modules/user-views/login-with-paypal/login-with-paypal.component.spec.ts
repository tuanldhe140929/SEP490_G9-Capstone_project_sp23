import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LoginWithPaypalComponent } from './login-with-paypal.component';

describe('LoginWithPaypalComponent', () => {
  let component: LoginWithPaypalComponent;
  let fixture: ComponentFixture<LoginWithPaypalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LoginWithPaypalComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LoginWithPaypalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
