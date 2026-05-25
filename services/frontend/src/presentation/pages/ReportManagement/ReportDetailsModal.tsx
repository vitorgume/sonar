import React from 'react';
import { Modal } from '../../components/ui/Modal';
import { Button } from '../../components/ui/Button';
import { type Report } from '../../../domain/models/Report';
import { Sparkles, Clock, CheckCircle } from 'lucide-react';

interface ReportDetailsModalProps {
  isOpen: boolean;
  onClose: () => void;
  report: Report | null;
}

export const ReportDetailsModal: React.FC<ReportDetailsModalProps> = ({ isOpen, onClose, report }) => {
  if (!report) return null;

  const isCompleted = report.status === 'COMPLETED';

  return (
    <Modal isOpen={isOpen} onClose={onClose} title="Detalhes do Relatório">
      <div className="flex flex-col gap-6">
        
        {/* Header Info */}
        <div className="flex flex-col gap-2 border-b border-slate-100 pb-4">
          <div className="flex justify-between items-start">
            <h3 className="text-xl font-bold text-slate-900">{report.title}</h3>
            <span className={`px-2 py-1 text-xs font-semibold rounded-full flex items-center gap-1 ${
              isCompleted 
                ? 'bg-green-50 text-green-700 border border-green-200' 
                : 'bg-blue-50 text-blue-700 border border-blue-200'
            }`}>
              {isCompleted ? <CheckCircle size={14} /> : <Clock size={14} />}
              {report.status}
            </span>
          </div>
          <div className="text-sm text-slate-600 flex flex-col gap-1 mt-2">
            <p><span className="font-semibold text-slate-900">Cliente:</span> {report.client.name}</p>
            <p><span className="font-semibold text-slate-900">Responsável:</span> {report.user.name}</p>
            <p><span className="font-semibold text-slate-900">Data:</span> {new Date(report.creationDate).toLocaleString()}</p>
          </div>
        </div>

        {/* AI Analysis Section */}
        <div className="flex flex-col gap-2">
          <div className="flex items-center gap-2 text-purple-700 font-semibold mb-1">
            <Sparkles size={18} />
            <h4>Análise da Inteligência Artificial</h4>
          </div>
          {isCompleted ? (
            <div className="bg-purple-50 border border-purple-200 rounded-lg p-4 text-sm text-slate-800 leading-relaxed whitespace-pre-wrap">
              {report.analysis || 'Nenhuma análise gerada para este relatório.'}
            </div>
          ) : (
            <div className="bg-slate-50 border border-slate-200 rounded-lg p-6 flex flex-col items-center justify-center text-slate-500 gap-2">
              <Clock size={24} className="animate-pulse text-blue-500" />
              <p className="text-sm font-medium">A IA está processando a transcrição...</p>
            </div>
          )}
        </div>

        {/* Transcript Section */}
        <div className="flex flex-col gap-2">
          <h4 className="font-semibold text-slate-900">Transcrição Original</h4>
          <div className="bg-slate-50 border border-slate-200 rounded-lg p-4 max-h-48 overflow-y-auto text-sm text-slate-700 whitespace-pre-wrap font-mono">
            {report.transcript || 'Transcrição não disponível.'}
          </div>
        </div>

        <div className="flex justify-end pt-2 border-t border-slate-100">
          <Button variant="secondary" onClick={onClose}>Fechar</Button>
        </div>
      </div>
    </Modal>
  );
};
