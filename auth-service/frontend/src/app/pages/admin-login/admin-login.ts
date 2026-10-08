import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-admin-login',
  standalone: true,
  imports: [FormsModule, RouterLink],
  templateUrl: './admin-login.html',
  styleUrl: './admin-login.css'
})
export class AdminLoginComponent {

  email = '';
  password = '';
  errorMessage = '';

  constructor(private auth: AuthService, private router: Router) { }

  login(): void {
    this.errorMessage = '';
    this.auth.login(this.email, this.password).subscribe({
      next: (r) => {
        if (r.role !== 'ADMIN') {
          this.auth.logout();
          this.errorMessage = 'Only admin can login here.';
          return;
        }
        this.router.navigate(['/dashboard']);
      },
      error: (e) => this.errorMessage = e.status === 404
        ? 'Invalid input. No account found for this email.'
        : 'Wrong password.'
    });
  }
}
