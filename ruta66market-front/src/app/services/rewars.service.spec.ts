import { TestBed } from '@angular/core/testing';

import { RewarsService } from './rewars.service';

describe('RewarsService', () => {
  let service: RewarsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RewarsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
