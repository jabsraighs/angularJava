import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Devis, DevisRequest } from '../models/devis';

@Injectable({ providedIn: 'root' })
export class DevisService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = '/api/devis';

  getAll() {
    return this.http.get<Devis[]>(this.baseUrl);
  }

  getById(id: number) {
    return this.http.get<Devis>(`${this.baseUrl}/${id}`);
  }

  create(payload: DevisRequest) {
    return this.http.post<Devis>(this.baseUrl, payload);
  }

  update(id: number, payload: DevisRequest) {
    return this.http.put<Devis>(`${this.baseUrl}/${id}`, payload);
  }

  delete(id: number) {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}