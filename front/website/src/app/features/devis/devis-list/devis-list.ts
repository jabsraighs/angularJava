import { Component, OnInit, inject, signal } from '@angular/core';
import { DecimalPipe, DatePipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Header } from '../../../shared/components/header/header';
import { Badge } from '../../../shared/ui/badge/badge';
import { DevisService } from '../../../core/services/devis.service';
import { Devis, StatutDevis } from '../../../core/models/devis';

@Component({
  selector: 'app-devis-list',
  standalone: true,
  imports: [Header, Badge, DecimalPipe, DatePipe, RouterLink],
  templateUrl: './devis-list.html',
})
export class DevisList implements OnInit {
  private readonly service = inject(DevisService);

  devisItems = signal<Devis[]>([]);
  isLoading = signal(true);

  ngOnInit(): void {
    this.isLoading.set(true);
    this.service.getAll().subscribe((data) => {
      this.devisItems.set(data);
      this.isLoading.set(false);
    });
  }

  deleteDevis(id: number): void {
    if (!confirm('Supprimer ce devis ?')) {
      return;
    }
    this.service.delete(id).subscribe(() => {
      this.devisItems.update((list) => list.filter((d) => d.id !== id));
    });
  }

  badgeTone(statut: StatutDevis): 'success' | 'warning' | 'danger' | 'neutral' {
    switch (statut) {
      case 'ACCEPTE': return 'success';
      case 'EN_ATTENTE': return 'warning';
      case 'REFUSE': return 'danger';
      default: return 'neutral';
    }
  }
}