export interface User {
  id: number;
  username: string;
  role: string;
}

export interface RegisterRequest {
  username: string;
  password: string;
  role: string;
}

export type RecordStatus = 'PENDING' | 'IN_PROGRESS' | 'DONE';

export interface OperationRecord {
  id: number;
  title: string;
  description: string;
  status: RecordStatus;
  ownerUsername: string;
  createdAt: string;
  updatedAt: string;
}

export interface RecordRequest {
  title: string;
  description: string;
  status: RecordStatus;
}
