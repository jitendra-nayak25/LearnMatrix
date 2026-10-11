import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { finalize } from 'rxjs';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule, RouterLink],
  templateUrl: './register.html',
  styleUrl: './register.css'
})
export class RegisterComponent {

  email = '';
  otp = '';
  otpSent = false;
  emailVerified = false;
  busy = false;
  resendIn = 0;
  private resendTimer: any;

  name = '';
  password = '';
  role: 'STUDENT' | 'FACULTY' = 'STUDENT';

  message = '';
  errorMessage = '';

  constructor(private auth: AuthService, private router: Router) { }

  private startCooldown(): void {
    this.resendIn = 60;
    clearInterval(this.resendTimer);
    this.resendTimer = setInterval(() => {
      this.resendIn--;
      if (this.resendIn <= 0) {
        clearInterval(this.resendTimer);
      }
    }, 1000);
  }

  sendOtp(): void {
    this.errorMessage = '';
    if (!this.email.trim()) {
      this.errorMessage = 'Enter email first.';
      return;
    }
    if (this.busy || this.resendIn > 0) {
      return;
    }
    this.busy = true;
    this.auth.sendOtp(this.email.trim())
      .pipe(finalize(() => this.busy = false))
      .subscribe({
        next: () => {
          this.otpSent = true;
          this.message = 'OTP sent to your email. Enter it below.';
          this.startCooldown();
        },
        error: (e) => {
          if (e.status === 429) {
            this.otpSent = true;
            this.errorMessage = 'OTP already sent. Please wait a minute before resending.';
            this.startCooldown();
          } else if (e.status === 0) {
            this.errorMessage = 'Cannot reach server. Start the backend first.';
          } else {
            this.errorMessage = 'Could not send OTP. Check email.';
          }
        }
      });
  }

  verifyOtp(): void {
    this.errorMessage = '';
    if (!this.otp) {
      this.errorMessage = 'Enter OTP.';
      return;
    }
    this.busy = true;
    this.auth.verifyOtp(this.email.trim(), this.otp)
      .pipe(finalize(() => this.busy = false))
      .subscribe({
        next: () => {
          this.emailVerified = true;
          this.message = 'Email verified. Now complete your details.';
        },
        error: (e) => this.errorMessage = e.status === 0
          ? 'Cannot reach server. Start the backend first.'
          : 'Invalid or expired OTP.'
      });
  }

  register(): void {
    this.errorMessage = '';
    if (!this.name.trim() || !this.password) {
      this.errorMessage = 'Enter name and password.';
      return;
    }
    this.busy = true;
    this.auth.register({
      name: this.name.trim(),
      email: this.email.trim(),
      password: this.password,
      role: this.role,
      otp: this.otp
    })
      .pipe(finalize(() => this.busy = false))
      .subscribe({
        next: () => this.router.navigate(['/login']),
        error: (e) => this.errorMessage = e.status === 0
          ? 'Cannot reach server. Start the backend first.'
          : 'Registration failed.'
      });
  }
}
