export interface AuthResponse {
  token: string;
  tokenType: string;
  username: string;
  role: string;
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface SessionUser {
  username: string;
  role: string;
  token: string;
}
