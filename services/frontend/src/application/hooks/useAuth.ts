import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { AuthService } from '../services/AuthService';
import type { LoginCredentials } from '../../domain/models/Auth';
import { useAuthContext } from '../../presentation/context/AuthContext';

export const useAuth = () => {
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();
  const { signIn, signOut: contextSignOut } = useAuthContext();

  const login = async (credentials: LoginCredentials) => {
    setIsLoading(true);
    setError(null);
    try {
      const response = await AuthService.login(credentials);
      
      // Salva no Contexto/LocalStorage através do AuthContext
      signIn({
        userId: response.userId,
        name: response.name || 'Usuário',
      });
      
      // Redirect to report management screen
      navigate('/reports');
    } catch (err: any) {
      setError(err.message || 'Erro ao realizar login');
    } finally {
      setIsLoading(false);
    }
  };

  const logout = () => {
    contextSignOut();
  };

  return {
    login,
    logout,
    isLoading,
    error,
  };
};
