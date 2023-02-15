import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeleteInspectorComponent } from './delete-inspector.component';

describe('DeleteInspectorComponent', () => {
  let component: DeleteInspectorComponent;
  let fixture: ComponentFixture<DeleteInspectorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DeleteInspectorComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DeleteInspectorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
