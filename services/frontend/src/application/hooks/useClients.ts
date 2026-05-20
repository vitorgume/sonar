import { useState, useEffect, useCallback } from 'react';
import { type Client, type User } from '../../domain/models/Client';
import { ClientMockService } from '../services/ClientMockService';

export const useClients = () => {
  const [clients, setClients] = useState<Client[]>([]);
  const [users, setUsers] = useState<User[]>([]);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  const fetchClients = useCallback(async () => {
    setIsLoading(true);
    setError(null);
    try {
      const data = await ClientMockService.getAll();
      setClients(data);
    } catch (err) {
      setError('Failed to fetch clients');
    } finally {
      setIsLoading(false);
    }
  }, []);

  const fetchUsers = useCallback(async () => {
    try {
      const data = await ClientMockService.getMockUsers();
      setUsers(data);
    } catch (err) {
      console.error('Failed to fetch users', err);
    }
  }, []);

  useEffect(() => {
    fetchClients();
    fetchUsers();
  }, [fetchClients, fetchUsers]);

  const createClient = async (data: Omit<Client, 'id' | 'creationDate'>) => {
    setIsLoading(true);
    try {
      const newClient = await ClientMockService.create(data);
      setClients((prev) => [...prev, newClient]);
      return newClient;
    } catch (err) {
      setError('Failed to create client');
      throw err;
    } finally {
      setIsLoading(false);
    }
  };

  const updateClient = async (id: string, data: Partial<Omit<Client, 'id' | 'creationDate'>>) => {
    setIsLoading(true);
    try {
      const updatedClient = await ClientMockService.update(id, data);
      setClients((prev) => prev.map((c) => (c.id === id ? updatedClient : c)));
      return updatedClient;
    } catch (err) {
      setError('Failed to update client');
      throw err;
    } finally {
      setIsLoading(false);
    }
  };

  const deleteClient = async (id: string) => {
    setIsLoading(true);
    try {
      await ClientMockService.delete(id);
      setClients((prev) => prev.filter((c) => c.id !== id));
    } catch (err) {
      setError('Failed to delete client');
      throw err;
    } finally {
      setIsLoading(false);
    }
  };

  return {
    clients,
    users,
    isLoading,
    error,
    createClient,
    updateClient,
    deleteClient,
    refreshClients: fetchClients
  };
};
