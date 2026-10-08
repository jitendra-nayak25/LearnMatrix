import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class LoginComponent {

  mode: 'password' | 'otp' = 'password';

  email = '';
  password = '';
  otp = '';
  otpSent = false;

  errorMessage = '';

  constructor(private auth: AuthService, private router: Router) { }

  sendOtp(): void {
    this.errorMessage = '';
    this.auth.sendOtp(this.email).subscribe({
      next: () => this.otpSent = true,
      error: () => this.errorMessage = 'Could not send OTP.'
    });
  }

  login(): void {
    this.errorMessage = '';
    const done = () => this.router.navigate(['/dashboard']);

    if (this.mode === 'password') {
      this.auth.login(this.email, this.password).subscribe({
        next: (r) => {
          if (r.role === 'ADMIN') {
            this.auth.logout();
            this.errorMessage = 'Admins must use the Admin Login page.';
            return;
          }
          done();
        },
        error: () => this.errorMessage = 'Invalid email or password'
      });
    } else {
      this.auth.loginWithOtp(this.email, this.otp).subscribe({
        next: (r) => {
          if (r.role === 'ADMIN') {
            this.auth.logout();
            this.errorMessage = 'Admins must use the Admin Login page.';
            return;
          }
          done();
        },
        error: () => this.errorMessage = 'Invalid or expired OTP'
      });
    }
  }
}
