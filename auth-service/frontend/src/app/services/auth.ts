import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

export interface RegisterRequest {
    name: string;
    email: string;
    password: string;
    role: string;
    otp: string;
}

export interface LoginResponse {
    token: string;
    email: string;
    role: string;
}

export interface AppUser {
    id: number;
    name: string;
    email: string;
    role: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {

    private apiUrl = 'http://localhost:8081/auth';

    constructor(private http: HttpClient) { }

    private headers() {
        return {
            headers: new HttpHeaders({
                Authorization: `Bearer ${this.getToken()}`
            })
        };
    }

    // Send OTP to email (used for register + OTP login).
    sendOtp(email: string): Observable<string> {
        return this.http.post(`${this.apiUrl}/otp/send`, { email }, { responseType: 'text' });
    }

    // Verify email OTP (registration step 2).
    verifyOtp(email: string, otp: string): Observable<string> {
        return this.http.post(`${this.apiUrl}/otp/verify`, { email, otp }, { responseType: 'text' });
    }

    register(data: RegisterRequest): Observable<any> {
        return this.http.post(`${this.apiUrl}/register`, data);
    }

    login(email: string, password: string): Observable<LoginResponse> {
        return this.http.post<LoginResponse>(`${this.apiUrl}/login`, { email, password })
            .pipe(tap(r => this.save(r)));
    }

    loginWithOtp(email: string, otp: string): Observable<LoginResponse> {
        return this.http.post<LoginResponse>(`${this.apiUrl}/login-otp`, { email, otp })
            .pipe(tap(r => this.save(r)));
    }

    private save(r: LoginResponse): void {
        if (r && r.token) {
            localStorage.setItem('token', r.token);
            localStorage.setItem('email', r.email);
            localStorage.setItem('role', r.role);
        }
    }

    // Admin only.
    getUsers(): Observable<AppUser[]> {
        return this.http.get<AppUser[]>(`${this.apiUrl}/users`, this.headers());
    }

    deleteUser(id: number): Observable<string> {
        return this.http.delete(`${this.apiUrl}/users/${id}`, { ...this.headers(), responseType: 'text' });
    }

    logout(): void {
        localStorage.clear();
    }

    getToken(): string | null { return localStorage.getItem('token'); }
    getEmail(): string | null { return localStorage.getItem('email'); }
    getRole(): string | null { return localStorage.getItem('role'); }
    isLoggedIn(): boolean { return !!this.getToken(); }
}
