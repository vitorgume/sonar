import React, { useState, useEffect } from 'react';
import { Modal } from '../../components/ui/Modal';
import { Input } from '../../components/ui/Input';
import { Button } from '../../components/ui/Button';
import { type Client } from '../../../domain/models/Client';

interface ClientFormModalProps {
  isOpen: boolean;
  onClose: () => void;
  onSubmit: (data: { name: string }) => Promise<void>;
  client?: Client | null;
  isLoading: boolean;
}

export const ClientFormModal: React.FC<ClientFormModalProps> = ({
  isOpen,
  onClose,
  onSubmit,
  client,
  isLoading
}) => {
  const [name, setName] = useState('');
  const [error, setError] = useState('');

  useEffect(() => {
    if (client) {
      setName(client.name);
    } else {
      setName('');
    }
    setError('');
  }, [client, isOpen]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!name.trim()) {
      setError('Client name is required');
      return;
    }
    
    try {
      await onSubmit({ name });
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
