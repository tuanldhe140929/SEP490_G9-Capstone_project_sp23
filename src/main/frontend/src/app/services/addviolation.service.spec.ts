import { TestBed } from '@angular/core/testing';

import { AddviolationService } from './addviolation.service';

describe('AddviolationService', () => {
  let service: AddviolationService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AddviolationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
