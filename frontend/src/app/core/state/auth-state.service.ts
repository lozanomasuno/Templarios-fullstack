import { inject } from '@angular/core';
import { signal, computed } from '@angular/core';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { SessionUser } from '../models/auth.model';

const SESSION_KEY = 'templarios_session';

@Injectable({ providedIn: 'root' })
export class AuthStateService {
  private readonly router = inject(Router);

  private readonly _user = signal<SessionUser | null>(this.loadFromStorage());

  readonly user = this._user.asReadonly();
  readonly isLoggedIn = computed(() => this._user() !== null);
  readonly isAdmin = computed(() => this._user()?.role === 'ROLE_ADMIN');
  readonly isOperator = computed(() => this._user()?.role === 'ROLE_OPERATOR');
  readonly token = computed(() => this._user()?.token ?? null);

  setUser(session: SessionUser): void {
    sessionStorage.setItem(SESSION_KEY, JSON.stringify(session));
    this._user.set(session);
  }

  clearUser(): void {
    sessionStorage.removeItem(SESSION_KEY);
    this._user.set(null);
    this.router.navigate(['/login']);
  }

  private loadFromStorage(): SessionUser | null {
    const raw = sessionStorage.getItem(SESSION_KEY);
    return raw ? JSON.parse(raw) : null;
  }
}
