import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { AuthResponse, LoginRequest, SessionUser } from '../models/auth.model';
import { AuthStateService } from '../state/auth-state.service';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly authState = inject(AuthStateService);
  private readonly baseUrl = environment.apiUrl;

  login(credentials: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.baseUrl}/api/auth/login`, credentials).pipe(
      tap((res) => {
        const session: SessionUser = {
          username: res.username,
          role: res.role,
          token: res.token,
        };
        this.authState.setUser(session);
      })
    );
  }

  logout(): void {
    this.authState.clearUser();
  }
}
