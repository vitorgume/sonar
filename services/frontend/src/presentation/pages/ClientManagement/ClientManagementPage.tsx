import React, { useState, useMemo } from 'react';
import { useClients } from '../../../application/hooks/useClients';
import { useAuthContext } from '../../context/AuthContext';
import { Button } from '../../components/ui/Button';
import { Table, Thead, Tbody, Tr, Th, Td } from '../../components/ui/Table';
import { Plus, Search, Edit2, Trash2 } from 'lucide-react';
import { ClientFormModal } from './ClientFormModal';
import { type Client } from '../../../domain/models/Client';

export const ClientManagementPage: React.FC = () => {
  const { clients, isLoading, createClient, updateClient, deleteClient } = useClients();
  const { user: authUser } = useAuthContext();
  const [searchTerm, setSearchTerm] = useState('');
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [clientToEdit, setClientToEdit] = useState<Client | null>(null);

  const filteredClients = useMemo(() => {
    return clients.filter((client) =>
      client.name.toLowerCase().includes(searchTerm.toLowerCase())
    );
  }, [clients, searchTerm]);

  const handleOpenModal = (client?: Client) => {
    setClientToEdit(client || null);
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setClientToEdit(null);
  };

  const handleSubmit = async (data: { name: string }) => {
    if (!authUser) return;
    
    const clientData = {
      name: data.name,
      user: {
        id: authUser.userId,
        name: authUser.name
      }
    };

    if (clientToEdit) {
      await updateClient(clientToEdit.id, clientData);
    } else {
      await createClient(clientData);
    }
  };

  const handleDelete = async (id: string) => {
    if (window.confirm('Are you sure you want to delete this client?')) {
      await deleteClient(id);
    }
  };

  return (
    <div className="p-8 max-w-7xl mx-auto flex flex-col gap-6">
      <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
        <div>
          <h1 className="text-2xl font-bold text-slate-900">Clients</h1>
          <p className="text-slate-600">Manage your customers and their assignments.</p>
        </div>
        <Button variant="primary" onClick={() => handleOpenModal()}>
          <Plus size={16} />
          New Client
        </Button>
      </div>

      <div className="flex items-center gap-2 max-w-md bg-white p-2 rounded-lg border border-slate-200 shadow-sm">
        <Search className="text-slate-400 ml-2" size={20} />
        <input
          type="text"
          placeholder="Search clients..."
          className="w-full border-none focus:ring-0 text-sm p-1 outline-none text-slate-900"
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
        />
      </div>

      {isLoading && clients.length === 0 ? (
        <div className="flex justify-center p-8">
          <p className="text-slate-500">Loading clients...</p>
        </div>
      ) : (
        <Table>
          <Thead>
            <Tr>
              <Th>Name</Th>
              <Th>Assigned User</Th>
              <Th>Created At</Th>
              <Th className="text-right">Actions</Th>
            </Tr>
          </Thead>
          <Tbody>
            {filteredClients.length > 0 ? (
              filteredClients.map((client) => (
                <Tr key={client.id}>
                  <Td className="font-medium text-slate-900">{client.name}</Td>
                  <Td>
                    <div className="flex flex-col">
                      <span className="text-slate-900">{client.user.name}</span>
                      <span className="text-xs text-slate-500">{client.user.email}</span>
                    </div>
                  </Td>
                  <Td>{new Date(client.creationDate).toLocaleDateString()}</Td>
                  <Td>
                    <div className="flex justify-end gap-2">
                      <Button variant="secondary" onClick={() => handleOpenModal(client)} aria-label="Edit">
                        <Edit2 size={14} />
                      </Button>
                      <Button variant="danger" onClick={() => handleDelete(client.id)} aria-label="Delete">
                        <Trash2 size={14} />
                      </Button>
                    </div>
                  </Td>
                </Tr>
              ))
            ) : (
              <Tr>
                <Td className="text-center py-8 text-slate-500" colSpan={4}>
                  No clients found.
                </Td>
              </Tr>
            )}
          </Tbody>
        </Table>
      )}

      <ClientFormModal
        isOpen={isModalOpen}
        onClose={handleCloseModal}
        onSubmit={handleSubmit}
        client={clientToEdit}
        isLoading={isLoading}
      />
    </div>
  );
};
