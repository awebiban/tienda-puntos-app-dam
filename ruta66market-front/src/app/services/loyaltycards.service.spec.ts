import { TestBed } from '@angular/core/testing';

import { LoyaltycardsService } from './loyaltycards.service';

describe('LoyaltycardsService', () => {
  let service: LoyaltycardsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LoyaltycardsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
