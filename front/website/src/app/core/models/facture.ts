export type StatutFacture = 'BROUILLON' | 'ENVOYEE' | 'PAYEE' | 'EN_RETARD';

export interface Facture {
  id: number;
  numero: string;
  clientNom: string;
  montantHT: number;
  montantTTC: number;
  statut: StatutFacture;
  dateEmission: string;
  dateEcheance: string;
}
