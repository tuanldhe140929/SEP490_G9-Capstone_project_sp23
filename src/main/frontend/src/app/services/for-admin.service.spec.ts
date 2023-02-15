import { TestBed } from '@angular/core/testing';

import { ForAdminService } from './for-admin.service';

describe('ForAdminService', () => {
  let service: ForAdminService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ForAdminService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
