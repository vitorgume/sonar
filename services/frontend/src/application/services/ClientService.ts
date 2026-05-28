import type { Client, User } from '../../domain/models/Client';

// Expected response structure based on ResponseDto.java
interface ApiResponse<T> {
  dado: T;
  erro: { mensagens: string[] } | null;
}

export const ClientService = {
  getAll: async (): Promise<Client[]> => {
    const response = await fetch(` /clients`);
    if (!response.ok) throw new Error('Failed to fetch clients');
    const result: ApiResponse<Client[]> = await response.json();
    return result.dado;
  },

  getById: async (id: string): Promise<Client | undefined> => {
    const response = await fetch(` /clients/${id}`);
    if (!response.ok) throw new Error('Failed to fetch client');
    const result: ApiResponse<Client> = await response.json();
    return result.dado;
  },

  create: async (data: Omit<Client, 'id' | 'creationDate'>): Promise<Client> => {
    const response = await fetch(` /clients`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data),
    });
    if (!response.ok) throw new Error('Failed to create client');
    const result: ApiResponse<Client> = await response.json();
    return result.dado;
  },

  update: async (id: string, data: Partial<Omit<Client, 'id' | 'creationDate'>>): Promise<Client> => {
    const response = await fetch(` /clients/${id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data),
    });
    if (!response.ok) throw new Error('Failed to update client');
    const result: ApiResponse<Client> = await response.json();
    return result.dado;
  },

  delete: async (id: string): Promise<void> => {
    const response = await fetch(` /clients/${id}`, {
      method: 'DELETE',
    });
    if (!response.ok) throw new Error('Failed to delete client');
  },
  
  getUsers: async (): Promise<User[]> => {
    const response = await fetch(` /users`);
    if (!response.ok) throw new Error('Failed to fetch users');
    const result: ApiResponse<User[]> = await response.json();
    return result.dado;
  }
};
