import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BansellerComponent } from './banseller.component';

describe('BansellerComponent', () => {
  let component: BansellerComponent;
  let fixture: ComponentFixture<BansellerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BansellerComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BansellerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
