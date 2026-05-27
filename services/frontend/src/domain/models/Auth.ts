export interface LoginCredentials {
  email: string;
  password: string;
}

export interface AuthResponse {
  userId: string;
  name: string;
  token: string;
}
