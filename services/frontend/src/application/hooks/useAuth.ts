import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { AuthService } from '../services/AuthService';
import type { LoginCredentials } from '../../domain/models/Auth';

export const useAuth = () => {
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();

  const login = async (credentials: LoginCredentials) => {
    setIsLoading(true);
    setError(null);
    try {
      const response = await AuthService.login(credentials);
      // Here we could save token to localStorage or context
      localStorage.setItem('sonar_token', response.token);
      localStorage.setItem('sonar_user', JSON.stringify({ userId: response.userId, name: response.name }));
      
      // Redirect to report management screen
      navigate('/reports');
    } catch (err: any) {
      setError(err.message || 'Erro ao realizar login');
    } finally {
      setIsLoading(false);
    }
  };

  const logout = () => {
    localStorage.removeItem('sonar_token');
    localStorage.removeItem('sonar_user');
    navigate('/login');
  };

  return {
    login,
    logout,
    isLoading,
    error,
  };
};
