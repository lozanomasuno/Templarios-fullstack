import { Component, inject, input, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { RecordService } from '../../core/services/record.service';
import { OperationRecord, RecordRequest, RecordStatus } from '../../core/models/domain.model';

@Component({
  selector: 'app-records-table',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <div class="section">
      <div class="section-header">
        <h2>{{ isAdmin() ? 'Todos los Registros' : 'Mis Registros' }}</h2>
        <button class="btn-primary" (click)="openCreate()">+ Nuevo Registro</button>
      </div>

      @if (showForm()) {
        <div class="form-card">
          <h3>{{ editingId() ? 'Editar Registro' : 'Nuevo Registro' }}</h3>
          <form [formGroup]="form" (ngSubmit)="submit()">
            <div class="field">
              <label>Título</label>
              <input formControlName="title" placeholder="Título del registro" />
            </div>
            <div class="field">
              <label>Descripción</label>
              <textarea formControlName="description" rows="3" placeholder="Descripción detallada..."></textarea>
            </div>
            <div class="field">
              <label>Estado</label>
              <select formControlName="status">
                <option value="PENDING">Pendiente</option>
                <option value="IN_PROGRESS">En Progreso</option>
                <option value="DONE">Completado</option>
              </select>
            </div>
            <div class="form-actions">
              <button type="button" class="btn-secondary" (click)="showForm.set(false)">Cancelar</button>
              <button type="submit" class="btn-primary" [disabled]="saving()">
                {{ saving() ? 'Guardando...' : 'Guardar' }}
              </button>
            </div>
          </form>
        </div>
      }

      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Título</th>
            <th>Estado</th>
            @if (isAdmin()) { <th>Operario</th> }
            <th>Actualizado</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          @for (r of records(); track r.id) {
            <tr>
              <td>#{{ r.id }}</td>
              <td>
                <strong>{{ r.title }}</strong>
                @if (r.description) { <br/><small>{{ r.description }}</small> }
              </td>
              <td><span class="badge" [class]="statusClass(r.status)">{{ statusLabel(r.status) }}</span></td>
              @if (isAdmin()) { <td>{{ r.ownerUsername }}</td> }
              <td>{{ r.updatedAt | date:'dd/MM/yy HH:mm' }}</td>
              <td class="actions">
                <button class="btn-icon" (click)="openEdit(r)">✏️</button>
                @if (isAdmin()) {
                  <button class="btn-icon danger" (click)="deleteRecord(r.id)">🗑️</button>
                }
              </td>
            </tr>
          }
          @empty {
            <tr><td colspan="6" class="empty">Sin registros aún</td></tr>
          }
        </tbody>
      </table>
    </div>
  `,
  styleUrl: './records-table.component.scss',
})
export class RecordsTableComponent implements OnInit {
  private readonly recordService = inject(RecordService);
  private readonly fb = inject(FormBuilder);

  readonly isAdmin = input(false);

  readonly records = signal<OperationRecord[]>([]);
  readonly showForm = signal(false);
  readonly saving = signal(false);
  readonly editingId = signal<number | null>(null);

  form = this.fb.group({
    title: ['', [Validators.required, Validators.maxLength(100)]],
    description: [''],
    status: ['PENDING' as RecordStatus, Validators.required],
  });

  ngOnInit(): void {
    this.loadRecords();
  }

  loadRecords(): void {
    this.recordService.getAll().subscribe({ next: (r) => this.records.set(r) });
  }

  openCreate(): void {
    this.editingId.set(null);
    this.form.reset({ status: 'PENDING' });
    this.showForm.set(true);
  }

  openEdit(record: OperationRecord): void {
    this.editingId.set(record.id);
    this.form.patchValue({ title: record.title, description: record.description, status: record.status });
    this.showForm.set(true);
  }

  submit(): void {
    if (this.form.invalid) return;
    this.saving.set(true);
    const payload = this.form.value as RecordRequest;
    const id = this.editingId();
    const obs = id
      ? this.recordService.update(id, payload)
      : this.recordService.create(payload);

    obs.subscribe({
      next: () => {
        this.saving.set(false);
        this.showForm.set(false);
        this.loadRecords();
      },
      error: () => this.saving.set(false),
    });
  }

  deleteRecord(id: number): void {
    if (!confirm('¿Eliminar este registro?')) return;
    this.recordService.delete(id).subscribe({ next: () => this.loadRecords() });
  }

  statusClass(status: RecordStatus): string {
    return { PENDING: 'badge-pending', IN_PROGRESS: 'badge-progress', DONE: 'badge-done' }[status];
  }

  statusLabel(status: RecordStatus): string {
    return { PENDING: 'Pendiente', IN_PROGRESS: 'En Progreso', DONE: 'Completado' }[status];
  }
}
