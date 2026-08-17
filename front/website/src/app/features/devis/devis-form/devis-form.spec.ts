import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DevisForm } from './devis-form';

describe('DevisForm', () => {
  let component: DevisForm;
  let fixture: ComponentFixture<DevisForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DevisForm],
    }).compileComponents();

    fixture = TestBed.createComponent(DevisForm);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
