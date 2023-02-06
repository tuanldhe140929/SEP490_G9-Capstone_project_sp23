import { TestBed } from '@angular/core/testing';

import { ManageInspectorService } from './manage-inspector.service';

describe('ManageInspectorService', () => {
  let service: ManageInspectorService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ManageInspectorService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
