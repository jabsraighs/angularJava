import { Component, input } from '@angular/core';

@Component({
  selector: 'app-badge',
  standalone: true,
  templateUrl: './badge.html',
})
export class Badge {
  tone = input<'neutral' | 'success' | 'warning' | 'danger'>('neutral');
}
