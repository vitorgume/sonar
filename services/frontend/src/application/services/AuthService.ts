import type { AuthResponse, LoginCredentials } from '../../domain/models/Auth';

interface ApiResponse<T> {
  dado: T;
  erro: { mensagens: string[] } | null;
}

const API_BASE_URL = '/api';

export const AuthService = {
  login: async (credentials: LoginCredentials): Promise<AuthResponse> => {
    const response = await fetch(`${API_BASE_URL}/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(credentials),
    });
    
    if (!response.ok) {
      throw new Error('Falha no login. Verifique suas credenciais.');
    }
    
    const result: ApiResponse<AuthResponse> = await response.json();
    
    if (result.erro && result.erro.mensagens.length > 0) {
        throw new Error(result.erro.mensagens.join(', '));
    }

    return result.dado;
  },
};
