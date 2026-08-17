// core/services/facture.service.ts
import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Facture } from '../models/facture';

@Injectable({ providedIn: 'root' })
export class FactureService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = '/api/factures';

  getAll() {
    return this.http.get<Facture[]>(this.baseUrl);
  }
} 