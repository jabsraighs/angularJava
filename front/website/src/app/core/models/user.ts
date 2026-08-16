export interface User {
  id: number;
  nom: string;
  prenom: string;
  email: string;
}

export interface UserRequest {
  nom: string;
  prenom: string;
  email: string;
  motDePasse: string;
}
