import { useState, useEffect, useCallback } from 'react';
import { type Client } from '../../domain/models/Client';
import { ClientService } from '../services/ClientService';
import { useAuthContext } from '../../presentation/context/AuthContext';

export const useClients = () => {
  const [clients, setClients] = useState<Client[]>([]);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const { user } = useAuthContext();

  const fetchClients = useCallback(async () => {
    if (!user) {
      setClients([]);
      setIsLoading(false);
      return;
    }

    setIsLoading(true);
    setError(null);
    try {
      const data = await ClientService.getAll();
      setClients(data.filter((client) => client.user?.id === user.userId));
    } catch (err) {
      setError('Failed to fetch clients');
    } finally {
      setIsLoading(false);
    }
  }, [user]);

  useEffect(() => {
    fetchClients();
  }, [fetchClients]);

  const createClient = async (data: Omit<Client, 'id' | 'creationDate'>) => {
    setIsLoading(true);
    try {
      const newClient = await ClientService.create(data);
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
      const updatedClient = await ClientService.update(id, data);
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
      await ClientService.delete(id);
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
    users, // <-- SOLUÇÃO: Exportando a variável users aqui!
    isLoading,
    error,
    createClient,
    updateClient,
    deleteClient,
    refreshClients: fetchClients,
    refreshUsers: fetchUsers // Adicionado também caso precise recarregar os usuários depois
  };
};