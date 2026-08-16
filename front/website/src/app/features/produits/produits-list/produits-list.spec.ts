import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProduitsList } from './produits-list';

describe('ProduitsList', () => {
  let component: ProduitsList;
  let fixture: ComponentFixture<ProduitsList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProduitsList]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProduitsList);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
