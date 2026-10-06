import { Component } from '@angular/core';

import {
  Router
} from '@angular/router';

import {
  AuthService
} from '../../services/auth';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class DashboardComponent {

  email: string | null;
  role: string | null;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {

    this.email =
      this.authService.getEmail();

    this.role =
      this.authService.getRole();
  }

  logout(): void {

    this.authService.logout();

    this.router.navigate(['/login']);
  }
}