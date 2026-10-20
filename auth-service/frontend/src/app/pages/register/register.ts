import { ChangeDetectorRef, Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService, serverMessage } from '../../services/auth';

type Stage = 'email' | 'otp' | 'details';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule, RouterLink],
  templateUrl: './register.html',
  styleUrl: './register.css'
})
export class RegisterComponent {

  stage: Stage = 'email';

  email = '';
  otp = '';
  name = '';
  password = '';
  role: 'STUDENT' | 'FACULTY' = 'STUDENT';

  info = '';
  error = '';

  constructor(
    private auth: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) { }

  sendOtp(): void {
    this.error = '';
    if (!this.email.trim()) {
      this.error = 'Enter email first.';
      return;
    }
    this.stage = 'otp';
    this.info = 'Sending OTP to your email...';
    this.auth.sendOtp(this.email.trim()).subscribe({
      next: () => {
        this.info = 'OTP sent. Enter it below.';
        this.cdr.markForCheck();
      },
      error: (e) => {
        if (e.status === 429) {
          this.info = 'OTP already sent. Enter it below.';
        } else if (e.status === 0) {
          this.stage = 'email';
          this.info = '';
          this.error = 'Cannot reach server.';
        } else {
          this.info = '';
          this.error = serverMessage(e, 'Could not send OTP.');
        }
        this.cdr.markForCheck();
      }
    });
  }

  verify(): void {
    this.error = '';
    if (!this.otp) {
      this.error = 'Enter OTP.';
      return;
    }
    this.auth.verifyOtp(this.email.trim(), this.otp).subscribe({
      next: () => {
        this.stage = 'details';
        this.info = 'Email verified. Complete your details.';
        this.cdr.markForCheck();
      },
      error: (e) => {
        if (e.status === 0) {
          this.error = 'Cannot reach server.';
        } else {
          this.error = serverMessage(e, 'Invalid or expired OTP.')
            + ' Use the newest mail, or tap Resend OTP.';
        }
        this.cdr.markForCheck();
      }
    });
  }

  register(): void {
    this.error = '';
    if (!this.name.trim() || !this.password) {
      this.error = 'Enter name and password.';
      return;
    }
    this.auth.register({
      name: this.name.trim(),
      email: this.email.trim(),
      password: this.password,
      role: this.role,
      otp: this.otp
    }).subscribe({
      next: () => {
        this.router.navigate(['/login']);
      },
      error: (e) => {
        if (e.status === 0) {
          this.error = 'Cannot reach server.';
        } else {
          this.error = serverMessage(e, 'Registration failed.');
        }
        this.cdr.markForCheck();
      }
    });
  }
}
