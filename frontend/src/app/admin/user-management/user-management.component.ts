import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { UserService } from '../../core/services/user.service';
import { User } from '../../core/models/domain.model';

@Component({
  selector: 'app-user-management',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <div class="section">
      <div class="section-header">
        <h2>Gestión de Usuarios</h2>
        <button class="btn-primary" (click)="showForm.set(!showForm())">
          {{ showForm() ? 'Cancelar' : '+ Nuevo Usuario' }}
        </button>
      </div>

      @if (showForm()) {
        <div class="form-card">
          <h3>Registrar Usuario</h3>
          <form [formGroup]="form" (ngSubmit)="submit()">
            <div class="fields-row">
              <div class="field">
                <label>Usuario</label>
                <input formControlName="username" placeholder="nombre_usuario" />
              </div>
              <div class="field">
                <label>Contraseña</label>
                <input formControlName="password" type="password" placeholder="••••••" />
              </div>
              <div class="field">
                <label>Rol</label>
                <select formControlName="role">
                  <option value="ROLE_OPERATOR">Operario</option>
                  <option value="ROLE_ADMIN">Administrador</option>
                </select>
              </div>
            </div>
            @if (errorMsg()) {
              <p class="error">{{ errorMsg() }}</p>
            }
            <button type="submit" class="btn-primary" [disabled]="saving()">
              {{ saving() ? 'Guardando...' : 'Guardar' }}
            </button>
          </form>
        </div>
      }

      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Usuario</th>
            <th>Rol</th>
          </tr>
        </thead>
        <tbody>
          @for (u of users(); track u.id) {
            <tr>
              <td>{{ u.id }}</td>
              <td>{{ u.username }}</td>
              <td><span class="badge" [class]="roleBadge(u.role)">{{ roleLabel(u.role) }}</span></td>
            </tr>
          }
        </tbody>
      </table>
    </div>
  `,
  styleUrl: './user-management.component.scss',
})
export class UserManagementComponent implements OnInit {
  private readonly userService = inject(UserService);
  private readonly fb = inject(FormBuilder);

  readonly users = signal<User[]>([]);
  readonly showForm = signal(false);
  readonly saving = signal(false);
  readonly errorMsg = signal('');

  form = this.fb.group({
    username: ['', [Validators.required, Validators.minLength(3)]],
    password: ['', [Validators.required, Validators.minLength(6)]],
    role: ['ROLE_OPERATOR', Validators.required],
  });

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    this.userService.getAll().subscribe({ next: (u) => this.users.set(u) });
  }

  submit(): void {
    if (this.form.invalid) return;
    this.saving.set(true);
    this.errorMsg.set('');
    const { username, password, role } = this.form.value;
    this.userService.register({ username: username!, password: password!, role: role! }).subscribe({
      next: () => {
        this.saving.set(false);
        this.showForm.set(false);
        this.form.reset({ role: 'ROLE_OPERATOR' });
        this.loadUsers();
      },
      error: (err) => {
        this.saving.set(false);
        this.errorMsg.set(err.error?.message ?? 'Error al crear usuario');
      },
    });
  }

  roleBadge(role: string): string {
    return role === 'ROLE_ADMIN' ? 'badge-admin' : 'badge-operator';
  }

  roleLabel(role: string): string {
    return role === 'ROLE_ADMIN' ? 'Admin' : 'Operario';
  }
}
