import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { User } from '../../../core/models/user';
import { UserService } from '../../../core/services/user.service';

@Component({
  selector: 'app-user-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './user-list.html',
  styleUrl: './user-list.css',
})
export class UserList implements OnInit {
  private readonly userService = inject(UserService);

  users: User[] = [];
  isLoading = true;
  errorMessage: string | null = null;

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
  console.log('loadUsers appelé');
  this.isLoading = true;
  this.errorMessage = null;

  this.userService.getAll().subscribe({
    next: (users) => {
      console.log('Données reçues:', users);
      this.users = users;
      this.isLoading = false;
      console.log('isLoading après:', this.isLoading);
    },
    error: (err) => {
      console.log('Erreur reçue:', err);
      this.errorMessage = 'Impossible de charger les utilisateurs.';
      this.isLoading = false;
    },
  });
}
  deleteUser(id: number): void {
    if (!confirm('Supprimer cet utilisateur ?')) {
      return;
    }
    this.userService.delete(id).subscribe(() => {
      this.users = this.users.filter((u) => u.id !== id);
    });
  }
}
