import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProduitsForm } from './produits-form';

describe('ProduitsForm', () => {
  let component: ProduitsForm;
  let fixture: ComponentFixture<ProduitsForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProduitsForm]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProduitsForm);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
