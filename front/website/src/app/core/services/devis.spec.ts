import { TestBed } from '@angular/core/testing';

import { Devis } from './devis';

describe('Devis', () => {
  let service: Devis;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Devis);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
