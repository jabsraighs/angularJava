import { Component, OnInit, inject, signal } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Header } from '../../../shared/components/header/header';
import { Button } from '../../../shared/ui/button/button';
import { FactureService } from '../../../core/services/facture.service';
import { DevisService } from '../../../core/services/devis.service';
import { Devis } from '../../../core/models/devis';

@Component({
  selector: 'app-facture-form',
  standalone: true,
  imports: [Header, Button, RouterLink],
  templateUrl: './facture-form.html',
})
export class FactureForm implements OnInit {
  private readonly factureService = inject(FactureService);
  private readonly devisService = inject(DevisService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);

  devis = signal<Devis | null>(null);
  dateEcheance = signal('');
  isLoading = signal(true);
  errorMessage = signal<string | null>(null);

  ngOnInit(): void {
    const devisId = Number(this.route.snapshot.paramMap.get('devisId'));

    this.devisService.getById(devisId).subscribe({
      next: (devis) => {
        this.devis.set(devis);
        const echeance = new Date();
        echeance.setDate(echeance.getDate() + 30);
        this.dateEcheance.set(echeance.toISOString().slice(0, 10));
        this.isLoading.set(false);
      },
      error: () => {
        this.errorMessage.set('Devis introuvable.');
        this.isLoading.set(false);
      },
    });
  }

  confirm(): void {
    const devis = this.devis();
    if (!devis) return;

    this.errorMessage.set(null);
    this.isLoading.set(true);

    this.factureService.create({ devisId: devis.id, dateEcheance: this.dateEcheance() }).subscribe({
      next: () => this.router.navigate(['/factures']),
      error: (err) => {
        this.isLoading.set(false);
        this.errorMessage.set(err.error?.message ?? 'Impossible de créer la facture.');
      },
    });
  }
}