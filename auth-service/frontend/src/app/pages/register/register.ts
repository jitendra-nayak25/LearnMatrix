import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
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

  name = '';
  password = '';
  role: 'STUDENT' | 'FACULTY' = 'STUDENT';

  message = '';
  errorMessage = '';

  constructor(private auth: AuthService, private router: Router) { }

  sendOtp(): void {
    this.errorMessage = '';
    this.auth.sendOtp(this.email).subscribe({
      next: () => {
        this.otpSent = true;
        this.message = 'OTP sent to your email';
      },
      error: () => this.errorMessage = 'Could not send OTP. Check email.'
    });
  }

  verifyOtp(): void {
    this.errorMessage = '';
    this.auth.verifyOtp(this.email, this.otp).subscribe({
      next: () => {
        this.emailVerified = true;
        this.message = 'Email verified. Now complete your details.';
      },
      error: () => this.errorMessage = 'Invalid or expired OTP.'
    });
  }

  register(): void {
    this.errorMessage = '';
    this.auth.register({
      name: this.name,
      email: this.email,
      password: this.password,
      role: this.role,
      otp: this.otp
    }).subscribe({
      next: () => this.router.navigate(['/login']),
      error: (e) => this.errorMessage = e.error || 'Registration failed.'
    });
  }
}
