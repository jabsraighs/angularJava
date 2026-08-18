import { Component, OnInit, inject, signal } from '@angular/core';
import { DecimalPipe, DatePipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Header } from '../../../shared/components/header/header';
import { Badge } from '../../../shared/ui/badge/badge';
import { FactureService } from '../../../core/services/facture.service';
import { Facture, StatutFacture } from '../../../core/models/facture';

@Component({
  selector: 'app-facture-list',
  standalone: true,
  imports: [Header, Badge, DecimalPipe, DatePipe, RouterLink],
  templateUrl: './facture-list.html',
})
export class FactureList implements OnInit {
  private readonly service = inject(FactureService);

  factures = signal<Facture[]>([]);
  isLoading = signal(true);

  ngOnInit(): void {
    this.loadFactures();
  }

  loadFactures(): void {
    this.isLoading.set(true);
    this.service.getAll().subscribe((data) => {
      this.factures.set(data);
      this.isLoading.set(false);
    });
  }

  deleteFacture(id: number): void {
    if (!confirm('Supprimer cette facture ?')) {
      return;
    }
    this.service.delete(id).subscribe(() => {
      this.factures.update((list) => list.filter((f) => f.id !== id));
    });
  }

  badgeTone(statut: StatutFacture): 'success' | 'warning' | 'danger' | 'neutral' {
    switch (statut) {
      case 'PAYEE': return 'success';
      case 'ENVOYEE': return 'warning';
      case 'EN_RETARD': return 'danger';
      default: return 'neutral';
    }
  }
}