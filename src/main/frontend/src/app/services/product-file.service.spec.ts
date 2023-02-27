import { TestBed } from '@angular/core/testing';

import { ProductFileService } from './product-file.service';

describe('ProductFileService', () => {
  let service: ProductFileService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProductFileService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
