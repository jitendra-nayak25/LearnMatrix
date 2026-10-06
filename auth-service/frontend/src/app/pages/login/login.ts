import { Component } from '@angular/core';
import {
  FormsModule
} from '@angular/forms';

import {
  Router,
  RouterLink
} from '@angular/router';

import {
  AuthService
} from '../../services/auth';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    FormsModule,
    RouterLink
  ],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class LoginComponent {

  email = '';
  password = '';

  errorMessage = '';

  constructor(
    private authService: AuthService,
    private router: Router
  ) { }

  login(): void {

    this.errorMessage = '';

    this.authService.login({
      email: this.email,
      password: this.password
    })
      .subscribe({

        next: () => {

          this.router.navigate(['/dashboard']);

        },

        error: () => {

          this.errorMessage =
            'Invalid email or password';

        }

      });
  }
}