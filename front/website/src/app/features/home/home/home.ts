// features/home/home.ts
import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { Button } from '../../../shared/ui/button/button';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [Button],
  templateUrl: './home.html',
})
export class Home {
  private readonly router = inject(Router);

  goTo(path: string): void {
    this.router.navigate([path]);
  }
}