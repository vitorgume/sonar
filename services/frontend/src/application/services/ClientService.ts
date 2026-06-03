import type { Client } from '../../domain/models/Client';
import type { User } from '../../presentation/context/AuthContext';
import { API_BASE_URL } from '../config/env';

interface ApiResponse<T> {
  dado: T;
  erro: { mensagens: string[] } | null;
}

export const ClientService = {
  getAll: async (): Promise<Client[]> => {
    const response = await fetch(`${API_BASE_URL}/clients`, {
      credentials: 'include' // <-- Adicionado
    });
    if (!response.ok) throw new Error('Failed to fetch clients');
    const result: ApiResponse<Client[]> = await response.json();
    return result.dado;
  },

  getById: async (id: string): Promise<Client | undefined> => {
    const response = await fetch(`${API_BASE_URL}/clients/${id}`, {
      credentials: 'include' // <-- Adicionado
    });
    if (!response.ok) throw new Error('Failed to fetch client');
    const result: ApiResponse<Client> = await response.json();
    return result.dado;
  },

  create: async (data: Omit<Client, 'id' | 'creationDate'>): Promise<Client> => {
    const response = await fetch(`${API_BASE_URL}/clients`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      credentials: 'include', // <-- Adicionado
      body: JSON.stringify(data),
    });
    if (!response.ok) throw new Error('Failed to create client');
    const result: ApiResponse<Client> = await response.json();
    return result.dado;
  },

  update: async (id: string, data: Partial<Omit<Client, 'id' | 'creationDate'>>): Promise<Client> => {
    const response = await fetch(`${API_BASE_URL}/clients/${id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      credentials: 'include', // <-- Adicionado
      body: JSON.stringify(data),
    });
    if (!response.ok) throw new Error('Failed to update client');
    const result: ApiResponse<Client> = await response.json();
    return result.dado;
  },

  delete: async (id: string): Promise<void> => {
    const response = await fetch(`${API_BASE_URL}/clients/${id}`, {
      method: 'DELETE',
      credentials: 'include' // <-- Adicionado
    });
    if (!response.ok) throw new Error('Failed to delete client');
  },
  
  getUsers: async (): Promise<User[]> => {
    const response = await fetch(`${API_BASE_URL}/users`, {
      credentials: 'include' // <-- Adicionado
    });
    if (!response.ok) throw new Error('Failed to fetch users');
    const result: ApiResponse<User[]> = await response.json();
    return result.dado;
  }
};