import { TestBed } from '@angular/core/testing';
import { ManageAccountInfoService } from './manage-account-info.service';



describe('UserService', () => {
  let service: ManageAccountInfoService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ManageAccountInfoService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
