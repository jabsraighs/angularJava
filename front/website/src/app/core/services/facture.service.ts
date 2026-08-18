import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Facture, FactureRequest } from '../models/facture';

@Injectable({ providedIn: 'root' })
export class FactureService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = '/api/factures';

  getAll() {
    return this.http.get<Facture[]>(this.baseUrl);
  }

  getById(id: number) {
    return this.http.get<Facture>(`${this.baseUrl}/${id}`);
  }

  create(payload: FactureRequest) {
    return this.http.post<Facture>(this.baseUrl, payload);
  }

  delete(id: number) {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}