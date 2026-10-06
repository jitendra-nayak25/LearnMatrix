import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

export interface RegisterRequest {
    name: string;
    email: string;
    password: string;
    role: string;
}

export interface RegisterResponse {
    message: string;
    email: string;
    role: string;
}

export interface LoginRequest {
    email: string;
    password: string;
}

export interface LoginResponse {
    token: string;
    email: string;
    role: string;
}

@Injectable({
    providedIn: 'root'
})
export class AuthService {

    private apiUrl = 'http://localhost:8081/auth';

    constructor(private http: HttpClient) { }

    register(user: RegisterRequest): Observable<RegisterResponse> {
        return this.http.post<RegisterResponse>(
            `${this.apiUrl}/register`,
            user
        );
    }

    login(credentials: LoginRequest): Observable<LoginResponse> {

        return this.http
            .post<LoginResponse>(
                `${this.apiUrl}/login`,
                credentials
            )
            .pipe(
                tap(response => {

                    if (response && response.token) {

                        localStorage.setItem(
                            'token',
                            response.token
                        );

                        localStorage.setItem(
                            'email',
                            response.email
                        );

                        localStorage.setItem(
                            'role',
                            response.role
                        );
                    }
                })
            );
    }

    logout(): void {
        localStorage.removeItem('token');
        localStorage.removeItem('email');
        localStorage.removeItem('role');
    }

    getToken(): string | null {
        return localStorage.getItem('token');
    }

    getEmail(): string | null {
        return localStorage.getItem('email');
    }

    getRole(): string | null {
        return localStorage.getItem('role');
    }

    isLoggedIn(): boolean {
        return !!this.getToken();
    }
}