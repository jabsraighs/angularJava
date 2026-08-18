import { Routes } from '@angular/router';
import { Home } from './features/home/home/home';
import { UserList } from './features/users/user-list/user-list';
import { UserForm } from './features/users/user-form/user-form';
import { FactureList } from './features/facture/facture-list/facture-list';
import { FactureForm } from './features/facture/facture-form/facture-form';
import { DevisList } from './features/devis/devis-list/devis-list';
import { DevisForm } from './features/devis/devis-form/devis-form';

export const routes: Routes = [
  { path: '', component: Home },
  { path: 'users', component: UserList },
  { path: 'users/nouveau', component: UserForm },
  { path: 'users/:id/modifier', component: UserForm },
  { path: 'factures', component: FactureList },
  { path: 'factures/nouveau/:devisId', component: FactureForm },
  { path: 'devis', component: DevisList },
  { path: 'devis/nouveau', component: DevisForm },
  { path: 'devis/:id/modifier', component: DevisForm },
];