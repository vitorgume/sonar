import React, { useState, useEffect } from 'react';
import { Modal } from '../../components/ui/Modal';
import { Input } from '../../components/ui/Input';
import { Button } from '../../components/ui/Button';
import { type Client, type User } from '../../../domain/models/Client';

interface ClientFormModalProps {
  isOpen: boolean;
  onClose: () => void;
  onSubmit: (data: { name: string; user: User }) => Promise<void>;
  client?: Client | null;
  users: User[];
  isLoading: boolean;
}

export const ClientFormModal: React.FC<ClientFormModalProps> = ({
  isOpen,
  onClose,
  onSubmit,
  client,
  users,
  isLoading
}) => {
  const [name, setName] = useState('');
  const [selectedUserId, setSelectedUserId] = useState('');
  const [error, setError] = useState('');

  useEffect(() => {
    if (client) {
      setName(client.name);
      setSelectedUserId(client.user.id);
    } else {
      setName('');
      setSelectedUserId(users.length > 0 ? users[0].id : '');
    }
    setError('');
  }, [client, isOpen, users]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!name.trim()) {
      setError('Client name is required');
      return;
    }
    
    const selectedUser = users.find(u => u.id === selectedUserId);
    if (!selectedUser) {
      setError('Please select a user');
      return;
    }

    try {
      await onSubmit({ name, user: selectedUser });
      onClose();
    } catch (err) {
      setError('Failed to save client');
    }
  };

  return (
    <Modal isOpen={isOpen} onClose={onClose} title={client ? 'Edit Client' : 'New Client'}>
      <form onSubmit={handleSubmit} className="flex flex-col gap-4">
        <Input
          label="Client Name"
          value={name}
          onChange={(e) => setName(e.target.value)}
          placeholder="e.g., Acme Corp"
          error={error}
        />
        
        <div className="flex flex-col gap-1 w-full">
          <label className="text-sm font-semibold text-slate-900">Assigned User</label>
          <select
            className="bg-white border border-slate-300 text-slate-900 rounded-md px-3 py-2 text-sm focus:outline-none focus:ring-1 focus:border-blue-500 focus:ring-blue-500 transition-colors"
            value={selectedUserId}
            onChange={(e) => setSelectedUserId(e.target.value)}
          >
            {users.map((user) => (
              <option key={user.id} value={user.id}>
                {user.name} ({user.email})
              </option>
            ))}
          </select>
        </div>

        <div className="flex justify-end gap-2 mt-4">
          <Button type="button" variant="secondary" onClick={onClose} disabled={isLoading}>
            Cancel
          </Button>
          <Button type="submit" variant="primary" disabled={isLoading}>
            {isLoading ? 'Saving...' : 'Save Client'}
          </Button>
        </div>
      </form>
    </Modal>
  );
};
