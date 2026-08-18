export type StatutDevis = 'EN_ATTENTE' | 'ACCEPTE' | 'REFUSE';

export interface Devis {
  id: number;
  numero: string;
  clientNom: string;
  montantHT: number;
  statut: StatutDevis;
  dateCreation: string;
  dateValidite: string;
}

export interface DevisRequest {
  clientNom: string;
  montantHT: number;
  statut: StatutDevis;
  dateValidite: string;
}