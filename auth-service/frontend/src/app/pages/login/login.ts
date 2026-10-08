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
  busy = false;

  errorMessage = '';

  constructor(private auth: AuthService, private router: Router) { }

  setMode(m: 'password' | 'otp'): void {
    this.mode = m;
    this.errorMessage = '';
    this.otpSent = false;
    this.otp = '';
  }

  sendOtp(): void {
    this.errorMessage = '';
    if (!this.email.trim()) {
      this.errorMessage = 'Enter email first.';
      return;
    }
    this.busy = true;
    this.auth.sendOtp(this.email.trim()).subscribe({
      next: () => { this.otpSent = true; this.busy = false; },
      error: () => { this.errorMessage = 'Could not send OTP.'; this.busy = false; }
    });
  }

  login(): void {
    this.errorMessage = '';
    if (!this.email.trim()) {
      this.errorMessage = 'Enter email first.';
      return;
    }
    if (this.mode === 'password' && !this.password) {
      this.errorMessage = 'Enter password.';
      return;
    }
    if (this.mode === 'otp' && !this.otp) {
      this.errorMessage = 'Enter OTP.';
      return;
    }
    this.busy = true;
    const done = () => { this.busy = false; this.router.navigate(['/dashboard']); };
    const fail = (msg: string) => { this.busy = false; this.errorMessage = msg; };

    if (this.mode === 'password') {
      this.auth.login(this.email.trim(), this.password).subscribe({
        next: (r) => {
          if (r.role === 'ADMIN') {
            this.auth.logout();
            fail('Admins must use the Admin Login page.');
            return;
          }
          done();
        },
        error: (e) => fail(e.status === 404
          ? 'Invalid input. No account found for this email.'
          : 'Wrong password.')
      });
    } else {
      this.auth.loginWithOtp(this.email.trim(), this.otp).subscribe({
        next: (r) => {
          if (r.role === 'ADMIN') {
            this.auth.logout();
            fail('Admins must use the Admin Login page.');
            return;
          }
          done();
        },
        error: (e) => fail(e.status === 404
          ? 'Invalid input. No account found for this email.'
          : 'Invalid or expired OTP')
      });
    }
  }
}
