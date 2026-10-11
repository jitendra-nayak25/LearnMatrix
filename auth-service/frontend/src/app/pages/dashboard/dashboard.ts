import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService, AppUser } from '../../services/auth';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class DashboardComponent implements OnInit {

  email: string | null;
  role: string | null;
  users: AppUser[] = [];
  filter = 'ALL';
  counts = { ALL: 0, STUDENT: 0, FACULTY: 0 };

  constructor(private auth: AuthService, private router: Router) {
    this.email = this.auth.getEmail();
    this.role = this.auth.getRole();
  }

  ngOnInit(): void {
    if (this.role === 'ADMIN') {
      this.loadUsers();
    }
  }

  loadUsers(): void {
    this.auth.getUsers().subscribe({
      next: (u) => {
        this.users = u;
        this.counts = {
          ALL: u.filter(x => x.role !== 'ADMIN').length,
          STUDENT: u.filter(x => x.role === 'STUDENT').length,
          FACULTY: u.filter(x => x.role === 'FACULTY').length
        };
      },
      error: () => {
        this.users = [];
        this.counts = { ALL: 0, STUDENT: 0, FACULTY: 0 };
      }
    });
  }

  shownUsers(): AppUser[] {
    if (this.filter === 'ALL') {
      return this.users.filter(u => u.role !== 'ADMIN');
    }
    return this.users.filter(u => u.role === this.filter);
  }

  remove(id: number): void {
    this.auth.deleteUser(id).subscribe(() => this.loadUsers());
  }

  logout(): void {
    this.auth.logout();
    this.router.navigate(['/login']);
  }
}
