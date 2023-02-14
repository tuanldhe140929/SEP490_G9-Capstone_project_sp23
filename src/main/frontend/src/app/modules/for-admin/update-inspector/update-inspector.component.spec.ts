import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateInspectorComponent } from './update-inspector.component';

describe('UpdateInspectorComponent', () => {
  let component: UpdateInspectorComponent;
  let fixture: ComponentFixture<UpdateInspectorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UpdateInspectorComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UpdateInspectorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
