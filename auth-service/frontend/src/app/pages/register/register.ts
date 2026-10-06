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
  selector: 'app-register',
  standalone: true,
  imports: [
    FormsModule,
    RouterLink
  ],
  templateUrl: './register.html',
  styleUrl: './register.css'
})
export class RegisterComponent {

  name = '';
  email = '';
  password = '';
  role = 'STUDENT';

  message = '';
  errorMessage = '';

  constructor(
    private authService: AuthService,
    private router: Router
  ) { }

  register(): void {

    this.message = '';
    this.errorMessage = '';

    this.authService.register({

      name: this.name,
      email: this.email,
      password: this.password,
      role: this.role

    })
      .subscribe({

        next: (response) => {

          this.message = response.message;

          setTimeout(() => {
            this.router.navigate(['/login']);
          }, 1000);

        },

        error: () => {

          this.errorMessage =
            'Registration failed. Email may already exist.';

        }

      });
  }
}