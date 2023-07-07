import { TestBed } from '@angular/core/testing';

import { BourceService } from './bource.service';

describe('BourceService', () => {
  let service: BourceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BourceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
