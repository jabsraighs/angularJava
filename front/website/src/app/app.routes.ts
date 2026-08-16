import { Routes } from '@angular/router';
import { UserList } from './features/users/user-list/user-list';
import { UserForm } from './features/users/user-form/user-form';

export const routes: Routes = [
  { path: '', redirectTo: 'users', pathMatch: 'full' },
  { path: 'users', component: UserList },
  { path: 'users/nouveau', component: UserForm },
  { path: 'users/:id/modifier', component: UserForm },
];
