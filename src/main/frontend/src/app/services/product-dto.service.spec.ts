import { TestBed } from '@angular/core/testing';

import { ProductDtoService } from './product-dto.service';

describe('ProductDtoService', () => {
  let service: ProductDtoService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProductDtoService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
