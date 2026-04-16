import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { OperationRecord, RecordRequest } from '../models/domain.model';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class RecordService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/api/records`;

  getAll(): Observable<OperationRecord[]> {
    return this.http.get<OperationRecord[]>(this.baseUrl);
  }

  create(payload: RecordRequest): Observable<OperationRecord> {
    return this.http.post<OperationRecord>(this.baseUrl, payload);
  }

  update(id: number, payload: RecordRequest): Observable<OperationRecord> {
    return this.http.put<OperationRecord>(`${this.baseUrl}/${id}`, payload);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
