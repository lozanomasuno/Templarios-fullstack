import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AuthStateService } from '../../core/state/auth-state.service';
import { AuthService } from '../../core/services/auth.service';
import { UserService } from '../../core/services/user.service';
import { RecordService } from '../../core/services/record.service';
import { User, OperationRecord } from '../../core/models/domain.model';
import { UserManagementComponent } from '../user-management/user-management.component';
import { RecordsTableComponent } from '../../shared/records-table/records-table.component';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule, UserManagementComponent, RecordsTableComponent],
  template: `
    <div class="layout">
      <aside class="sidebar">
        <div class="brand">⚔ Templarios</div>
        <nav>
          <a (click)="tab.set('overview')" [class.active]="tab() === 'overview'">Dashboard</a>
          <a (click)="tab.set('users')" [class.active]="tab() === 'users'">Usuarios</a>
          <a (click)="tab.set('records')" [class.active]="tab() === 'records'">Registros</a>
        </nav>
        <div class="user-info">
          <span>{{ authState.user()?.username }}</span>
          <small>Administrador</small>
          <button (click)="logout()">Cerrar sesión</button>
        </div>
      </aside>

      <main class="content">
        @if (tab() === 'overview') {
          <div class="overview">
            <h1>Panel Administrativo</h1>
            <div class="stats">
              <div class="stat-card">
                <span class="number">{{ users().length }}</span>
                <span class="label">Usuarios</span>
              </div>
              <div class="stat-card">
                <span class="number">{{ records().length }}</span>
                <span class="label">Registros totales</span>
              </div>
              <div class="stat-card">
                <span class="number">{{ pendingCount() }}</span>
                <span class="label">Pendientes</span>
              </div>
            </div>
          </div>
        }

        @if (tab() === 'users') {
          <app-user-management />
        }

        @if (tab() === 'records') {
          <app-records-table [isAdmin]="true" />
        }
      </main>
    </div>
  `,
  styleUrl: './admin-dashboard.component.scss',
})
export class AdminDashboardComponent implements OnInit {
  readonly authState = inject(AuthStateService);
  private readonly authService = inject(AuthService);
  private readonly userService = inject(UserService);
  private readonly recordService = inject(RecordService);

  readonly tab = signal<'overview' | 'users' | 'records'>('overview');
  readonly users = signal<User[]>([]);
  readonly records = signal<OperationRecord[]>([]);
  readonly pendingCount = signal(0);

  ngOnInit(): void {
    this.userService.getAll().subscribe({ next: (u) => this.users.set(u) });
    this.recordService.getAll().subscribe({
      next: (r) => {
        this.records.set(r);
        this.pendingCount.set(r.filter((x) => x.status === 'PENDING').length);
      },
    });
  }

  logout(): void {
    this.authService.logout();
  }
}
