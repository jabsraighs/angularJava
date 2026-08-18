export type StatutFacture = 'BROUILLON' | 'ENVOYEE' | 'PAYEE' | 'EN_RETARD';

export interface Facture {
  id: number;
  numero: string;
  devisId: number;
  clientNom: string;
  montantHT: number;
  montantTTC: number;
  statut: StatutFacture;
  dateEmission: string;
  dateEcheance: string;
}

export interface FactureRequest {
  devisId: number;
  dateEcheance: string;
}