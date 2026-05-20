import { type Client, type User } from '../../domain/models/Client';
import { v4 as uuidv4 } from 'uuid';

const MOCK_USERS: User[] = [
  { id: uuidv4(), name: 'John Doe', email: 'john@example.com' },
  { id: uuidv4(), name: 'Jane Smith', email: 'jane@example.com' },
];

let mockClients: Client[] = [
  {
    id: uuidv4(),
    name: 'Acme Corp',
    user: MOCK_USERS[0],
    creationDate: new Date().toISOString(),
  },
  {
    id: uuidv4(),
    name: 'Global Industries',
    user: MOCK_USERS[1],
    creationDate: new Date().toISOString(),
  }
];

export const ClientMockService = {
  getAll: async (): Promise<Client[]> => {
    return new Promise((resolve) => {
      setTimeout(() => resolve([...mockClients]), 300);
    });
  },

  getById: async (id: string): Promise<Client | undefined> => {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve(mockClients.find((c) => c.id === id));
      }, 200);
    });
  },

  create: async (data: Omit<Client, 'id' | 'creationDate'>): Promise<Client> => {
    return new Promise((resolve) => {
      const newClient: Client = {
        ...data,
        id: uuidv4(),
        creationDate: new Date().toISOString(),
      };
      mockClients = [...mockClients, newClient];
      setTimeout(() => resolve(newClient), 300);
    });
  },

  update: async (id: string, data: Partial<Omit<Client, 'id' | 'creationDate'>>): Promise<Client> => {
    return new Promise((resolve, reject) => {
      setTimeout(() => {
        const index = mockClients.findIndex((c) => c.id === id);
        if (index === -1) {
          reject(new Error('Client not found'));
          return;
        }
        
        const updatedClient = { ...mockClients[index], ...data };
        mockClients = [
          ...mockClients.slice(0, index),
          updatedClient,
          ...mockClients.slice(index + 1)
        ];
        resolve(updatedClient);
      }, 300);
    });
  },

  delete: async (id: string): Promise<void> => {
    return new Promise((resolve) => {
      setTimeout(() => {
        mockClients = mockClients.filter((c) => c.id !== id);
        resolve();
      }, 300);
    });
  },
  
  getMockUsers: async (): Promise<User[]> => {
    return new Promise((resolve) => {
      setTimeout(() => resolve([...MOCK_USERS]), 100);
    });
  }
};
