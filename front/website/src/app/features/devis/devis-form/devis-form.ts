import { Component, OnInit, inject, signal } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Header } from '../../../shared/components/header/header';
import { Input } from '../../../shared/ui/input/input';
import { Button } from '../../../shared/ui/button/button';
import { DevisService } from '../../../core/services/devis.service';
import { DevisRequest, StatutDevis } from '../../../core/models/devis';

@Component({
  selector: 'app-devis-form',
  standalone: true,
  imports: [Header, Input, Button, RouterLink],
  templateUrl: './devis-form.html',
})
export class DevisForm implements OnInit {
  private readonly service = inject(DevisService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);

  isEditMode = signal(false);
  devisId = signal<number | null>(null);
  isLoading = signal(false);
  errorMessage = signal<string | null>(null);

  clientNom = signal('');
  montantHT = signal('');
  statut = signal<StatutDevis>('EN_ATTENTE');
  dateValidite = signal('');

  readonly statuts: StatutDevis[] = ['EN_ATTENTE', 'ACCEPTE', 'REFUSE'];

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      const id = Number(idParam);
      this.isEditMode.set(true);
      this.devisId.set(id);
      this.isLoading.set(true);

      this.service.getById(id).subscribe({
        next: (devis) => {
          this.clientNom.set(devis.clientNom);
          this.montantHT.set(String(devis.montantHT));
          this.statut.set(devis.statut);
          this.dateValidite.set(devis.dateValidite);
          this.isLoading.set(false);
        },
        error: () => {
          this.errorMessage.set('Impossible de charger ce devis.');
          this.isLoading.set(false);
        },
      });
    }
  }

  submit(): void {
    this.errorMessage.set(null);

    const payload: DevisRequest = {
      clientNom: this.clientNom(),
      montantHT: Number(this.montantHT()),
      statut: this.statut(),
      dateValidite: this.dateValidite(),
    };

    if (!payload.clientNom || !payload.montantHT || !payload.dateValidite) {
      this.errorMessage.set('Merci de remplir tous les champs obligatoires.');
      return;
    }

    this.isLoading.set(true);

    const request$ = this.isEditMode()
      ? this.service.update(this.devisId()!, payload)
      : this.service.create(payload);

    request$.subscribe({
      next: () => {
        this.router.navigate(['/devis']);
      },
      error: () => {
        this.isLoading.set(false);
        this.errorMessage.set("Une erreur est survenue lors de l'enregistrement.");
      },
    });
  }
}