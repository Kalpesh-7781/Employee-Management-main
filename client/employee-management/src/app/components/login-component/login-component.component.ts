import { Component } from '@angular/core';
import { NgModel  } from '@angular/forms';

@Component({
  selector: 'app-login-component',
  standalone: true,
  imports: [],
  templateUrl: './login-component.component.html',
  styleUrl: './login-component.component.css'
})
export class LoginComponentComponent {
  email: string = '';
  password: string = '';
  error: string = '';

  login() {
    // Simulate authentication logic, replace it with your actual authentication logic
    if (this.email === 'test@example.com' && this.password === 'password') {
      alert('Login successful');
    } else {
      this.error = 'Invalid email or password';
    }
  }
}
