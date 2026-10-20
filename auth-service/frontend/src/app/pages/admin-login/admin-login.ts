import { ChangeDetectorRef, Component } from '@angular/core';
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
  loading = false;

  constructor(
    private auth: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) { }

  login(): void {
    this.errorMessage = '';
    this.loading = true;
    this.auth.login(this.email, this.password).subscribe({
      next: (r) => {
        this.loading = false;
        if (r.role !== 'ADMIN') {
          this.auth.logout();
          this.errorMessage = 'Only admin can login here.';
          this.cdr.markForCheck();
          return;
        }
        this.router.navigate(['/dashboard']);
      },
      error: (e) => {
        this.loading = false;
        this.errorMessage = e.status === 0
          ? 'Cannot reach server. Start the backend first.'
          : e.status === 404
            ? 'Invalid input. No account found for this email.'
            : 'Wrong password.';
        this.cdr.markForCheck();
      }
    });
  }
}
