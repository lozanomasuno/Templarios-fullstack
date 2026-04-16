import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../core/services/auth.service';
import { AuthStateService } from '../../core/state/auth-state.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  template: `
    <div class="login-wrapper">
      <div class="login-card">
        <h1>Templarios</h1>
        <h2>Iniciar Sesión</h2>

        <form [formGroup]="form" (ngSubmit)="onSubmit()">
          <div class="field">
            <label>Usuario</label>
            <input formControlName="username" type="text" placeholder="usuario" autocomplete="username" />
            @if (form.get('username')?.invalid && form.get('username')?.touched) {
              <span class="error">Campo obligatorio</span>
            }
          </div>

          <div class="field">
            <label>Contraseña</label>
            <input formControlName="password" type="password" placeholder="••••••" autocomplete="current-password" />
            @if (form.get('password')?.invalid && form.get('password')?.touched) {
              <span class="error">Campo obligatorio</span>
            }
          </div>

          @if (errorMsg()) {
            <div class="alert-error">{{ errorMsg() }}</div>
          }

          <button type="submit" [disabled]="loading()">
            {{ loading() ? 'Entrando...' : 'Entrar' }}
          </button>
        </form>

        <div class="hints">
          <small>Admin: admin / Admin123*</small><br />
          <small>Operario: operario1 / Oper123*</small>
        </div>
      </div>
    </div>
  `,
  styleUrl: './login.component.scss',
})
export class LoginComponent {
  private readonly fb = inject(FormBuilder);
  private readonly authService = inject(AuthService);
  private readonly authState = inject(AuthStateService);
  private readonly router = inject(Router);

  readonly loading = signal(false);
  readonly errorMsg = signal('');

  form = this.fb.group({
    username: ['', Validators.required],
    password: ['', Validators.required],
  });

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.loading.set(true);
    this.errorMsg.set('');

    const { username, password } = this.form.value;
    this.authService.login({ username: username!, password: password! }).subscribe({
      next: () => {
        const role = this.authState.user()?.role;
        this.router.navigate([role === 'ROLE_ADMIN' ? '/admin/dashboard' : '/operator/dashboard']);
      },
      error: () => {
        this.errorMsg.set('Credenciales incorrectas. Inténtalo de nuevo.');
        this.loading.set(false);
      },
    });
  }
}
