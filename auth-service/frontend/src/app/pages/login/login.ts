import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService, serverMessage } from '../../services/auth';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class LoginComponent {

  mode: 'password' | 'otp' = 'password';
  otpSent = false;

  email = '';
  password = '';
  otp = '';

  info = '';
  error = '';

  constructor(private auth: AuthService, private router: Router) { }

  setMode(m: 'password' | 'otp'): void {
    this.mode = m;
    this.info = '';
    this.error = '';
    this.otpSent = false;
    this.otp = '';
  }

  sendOtp(): void {
    this.error = '';
    if (!this.email.trim()) {
      this.error = 'Enter email first.';
      return;
    }
    this.otpSent = true;
    this.info = 'Sending OTP to your email...';
    this.auth.sendOtp(this.email.trim()).subscribe({
      next: () => this.info = 'OTP sent. Enter it below.',
        error: (e) => {
          if (e.status === 429) {
            this.info = 'OTP already sent. Enter it below.';
          } else if (e.status === 0) {
            this.otpSent = false;
            this.info = '';
            this.error = 'Cannot reach server.';
          } else {
            this.info = '';
            this.error = serverMessage(e, 'Could not send OTP.');
          }
        }
    });
  }

  login(): void {
    this.error = '';
    if (!this.email.trim()) {
      this.error = 'Enter email first.';
      return;
    }
    if (this.mode === 'password') {
      if (!this.password) {
        this.error = 'Enter password.';
        return;
      }
      this.auth.login(this.email.trim(), this.password).subscribe({
        next: (r) => {
          if (r?.role === 'ADMIN') {
            this.auth.logout();
            this.error = 'Admins must use the Admin Login page.';
            return;
          }
          this.router.navigate(['/dashboard']);
        },
        error: (e) => this.error = e.status === 0
          ? 'Cannot reach server.'
          : e.status === 404
            ? 'Invalid input. No account found for this email.'
            : 'Wrong password.'
      });
    } else {
      if (!this.otp) {
        this.error = 'Enter OTP.';
        return;
      }
      this.auth.loginWithOtp(this.email.trim(), this.otp).subscribe({
        next: (r) => {
          if (r?.role === 'ADMIN') {
            this.auth.logout();
            this.error = 'Admins must use the Admin Login page.';
            return;
          }
          this.router.navigate(['/dashboard']);
        },
        error: (e) => this.error = e.status === 0
          ? 'Cannot reach server.'
          : e.status === 404
            ? 'Invalid input. No account found for this email.'
            : serverMessage(e, 'Invalid or expired OTP.')
              + ' Use the newest mail, or tap Resend OTP.'
      });
    }
  }
}
