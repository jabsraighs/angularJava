// app.routes.ts
import { Routes } from '@angular/router';
import { Home } from './features/home/home/home';
import { UserList } from './features/users/user-list/user-list';
import { UserForm } from './features/users/user-form/user-form';
import { FactureList } from './features/facture/facture-list/facture-list';
import { DevisList } from './features/devis/devis-list/devis-list';

export const routes: Routes = [
  { path: '', component: Home },
  { path: 'users', component: UserList },
  { path: 'users/nouveau', component: UserForm },
  { path: 'users/:id/modifier', component: UserForm },
  { path: 'factures', component: FactureList },
  { path: 'devis', component: DevisList },
];