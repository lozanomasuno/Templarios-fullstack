import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthStateService } from '../../core/state/auth-state.service';
import { AuthService } from '../../core/services/auth.service';
import { RecordsTableComponent } from '../../shared/records-table/records-table.component';

@Component({
  selector: 'app-operator-dashboard',
  standalone: true,
  imports: [CommonModule, RecordsTableComponent],
  template: `
    <div class="layout">
      <aside class="sidebar operator">
        <div class="brand">⚔ Templarios</div>
        <nav>
          <a class="active">Mis Registros</a>
        </nav>
        <div class="user-info">
          <span>{{ authState.user()?.username }}</span>
          <small>Operario</small>
          <button (click)="logout()">Cerrar sesión</button>
        </div>
      </aside>

      <main class="content">
        <app-records-table [isAdmin]="false" />
      </main>
    </div>
  `,
  styleUrl: './operator-dashboard.component.scss',
})
export class OperatorDashboardComponent {
  readonly authState = inject(AuthStateService);
  private readonly authService = inject(AuthService);

  logout(): void {
    this.authService.logout();
  }
}
