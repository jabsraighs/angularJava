import { Component, input, output } from '@angular/core';

@Component({
  selector: 'app-button',
  standalone: true,
  templateUrl: './button.html',
})
export class Button {
  variant = input<'primary' | 'secondary' | 'danger' | 'ghost'>('primary');
  type = input<'button' | 'submit'>('button');
  disabled = input(false);
  clicked = output<void>();
}
