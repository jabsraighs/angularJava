import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FactureForm } from './facture-form';

describe('FactureForm', () => {
  let component: FactureForm;
  let fixture: ComponentFixture<FactureForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FactureForm],
    }).compileComponents();

    fixture = TestBed.createComponent(FactureForm);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
