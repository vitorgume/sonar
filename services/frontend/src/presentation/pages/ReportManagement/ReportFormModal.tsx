import React, { useState } from 'react';
import { Modal } from '../../components/ui/Modal';
import { Button } from '../../components/ui/Button';
import { Input } from '../../components/ui/Input';
import { useClients } from '../../../application/hooks/useClients';

interface ReportFormModalProps {
  isOpen: boolean;
  onClose: () => void;
  onSubmit: (title: string, clientId: string, transcript: string) => Promise<void>;
}

export const ReportFormModal: React.FC<ReportFormModalProps> = ({ isOpen, onClose, onSubmit }) => {
  const [title, setTitle] = useState('');
  const [clientId, setClientId] = useState('');
  const [transcript, setTranscript] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState('');
  
  const { clients, isLoading: isClientsLoading } = useClients();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!title.trim() || !clientId || !transcript.trim()) {
      setError('Todos os campos são obrigatórios.');
      return;
    }

    setIsSubmitting(true);
    setError('');
    try {
      await onSubmit(title, clientId, transcript);
      setTitle('');
      setClientId('');
      setTranscript('');
      onClose();
    } catch (err) {
      setError('Erro ao criar o relatório.');
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <Modal isOpen={isOpen} onClose={onClose} title="Novo Relatório de Call">
      <form onSubmit={handleSubmit} className="flex flex-col gap-4">
        {error && <div className="text-sm text-red-600 bg-red-50 p-2 rounded border border-red-200">{error}</div>}
        
        <Input
          label="Título da Call"
          placeholder="Ex: Sync de Alinhamento - Q1"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          disabled={isSubmitting}
        />

        <div className="flex flex-col gap-1 w-full">
          <label className="text-sm font-semibold text-slate-900">Cliente</label>
          <select
            className="bg-white border border-slate-300 focus:border-blue-500 focus:ring-blue-500 text-slate-900 rounded-md px-3 py-2 text-sm focus:outline-none focus:ring-1 transition-colors disabled:opacity-50"
            value={clientId}
            onChange={(e) => setClientId(e.target.value)}
            disabled={isSubmitting || isClientsLoading}
          >
            <option value="">Selecione um cliente</option>
            {clients.map((client) => (
              <option key={client.id} value={client.id}>
                {client.name}
              </option>
            ))}
          </select>
        </div>

        <div className="flex flex-col gap-1 w-full">
          <label className="text-sm font-semibold text-slate-900">Transcrição (Áudio da Call)</label>
          <textarea
            className="bg-white border border-slate-300 focus:border-blue-500 focus:ring-blue-500 text-slate-900 rounded-md px-3 py-2 text-sm focus:outline-none focus:ring-1 transition-colors resize-none disabled:opacity-50 h-32"
            placeholder="Cole aqui a transcrição da call..."
            value={transcript}
            onChange={(e) => setTranscript(e.target.value)}
            disabled={isSubmitting}
          />
        </div>

        <div className="flex justify-end gap-2 mt-4">
          <Button type="button" variant="secondary" onClick={onClose} disabled={isSubmitting}>
            Cancelar
          </Button>
          <Button type="submit" variant="primary" disabled={isSubmitting}>
            {isSubmitting ? 'Salvando...' : 'Analisar Call'}
          </Button>
        </div>
      </form>
    </Modal>
  );
};
