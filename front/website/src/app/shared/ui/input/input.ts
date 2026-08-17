import { Component, input, model } from '@angular/core';

@Component({
  selector: 'app-input',
  standalone: true,
  templateUrl: './input.html',
})
export class Input {
  label = input<string>('');
  type = input<'text' | 'email' | 'password' | 'number'>('text');
  placeholder = input<string>('');
  error = input<string | null>(null);
  value = model<string>('');
}