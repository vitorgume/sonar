import React, { useState } from 'react';
import { useReports } from '../../../application/hooks/useReports';
import { type Report } from '../../../domain/models/Report';
import { Button } from '../../components/ui/Button';
import { Table, Thead, Tbody, Tr, Th, Td } from '../../components/ui/Table';
import { Plus, Trash2, Eye, FileText, Loader2 } from 'lucide-react';
import { ReportFormModal } from './ReportFormModal';
import { ReportDetailsModal } from './ReportDetailsModal';

export const ReportManagementPage: React.FC = () => {
  const { reports, isLoading, createReport, deleteReport } = useReports();
  
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [selectedReport, setSelectedReport] = useState<Report | null>(null);

  const handleDelete = async (e: React.MouseEvent, id: string) => {
    e.stopPropagation();
    if (window.confirm('Tem certeza que deseja excluir este relatório?')) {
      await deleteReport(id);
    }
  };

  return (
    <div className="p-8 max-w-7xl mx-auto flex flex-col gap-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-slate-900">Relatórios de Calls</h1>
          <p className="text-slate-600 mt-1">Gerencie e analise as calls da sua equipe de vendas.</p>
        </div>
        <Button onClick={() => setIsFormOpen(true)}>
          <Plus size={18} />
          Novo Relatório
        </Button>
      </div>

      {isLoading && reports.length === 0 ? (
        <div className="flex items-center justify-center p-12 text-slate-500">
          <Loader2 className="animate-spin w-8 h-8" />
        </div>
      ) : (
        <Table>
          <Thead>
            <Tr>
              <Th>Título</Th>
              <Th>Cliente</Th>
              <Th>Data</Th>
              <Th>Status</Th>
              <Th className="text-right">Ações</Th>
            </Tr>
          </Thead>
          <Tbody>
            {reports.length === 0 ? (
              <Tr>
                <Td colSpan={5} className="text-center py-8 text-slate-500">
                  Nenhum relatório encontrado. Crie um novo para começar.
                </Td>
              </Tr>
            ) : (
              reports.map((report) => (
                <Tr 
                  key={report.id} 
                  className="cursor-pointer hover:bg-slate-50 transition-colors"
                  onClick={() => setSelectedReport(report)}
                >
                  <Td>
                    <div className="flex items-center gap-2 font-medium text-slate-900">
                      <FileText size={16} className="text-slate-400" />
                      {report.title}
                    </div>
                  </Td>
                  <Td>{report.client.name}</Td>
                  <Td>{new Date(report.creationDate).toLocaleDateString()}</Td>
                  <Td>
                    <span className={`px-2 py-1 text-xs font-semibold rounded-full ${
                      report.status === 'COMPLETED' 
                        ? 'bg-green-50 text-green-700 border border-green-200' 
                        : 'bg-blue-50 text-blue-700 border border-blue-200'
                    }`}>
                      {report.status}
                    </span>
                  </Td>
                  <Td className="text-right">
                    <div className="flex items-center justify-end gap-2">
                      <button 
                        className="p-2 text-slate-400 hover:text-blue-600 transition-colors rounded-md hover:bg-blue-50"
                        title="Ver Detalhes"
                      >
                        <Eye size={18} />
                      </button>
                      <button 
                        onClick={(e) => handleDelete(e, report.id)}
                        className="p-2 text-slate-400 hover:text-red-600 transition-colors rounded-md hover:bg-red-50"
                        title="Excluir"
                      >
                        <Trash2 size={18} />
                      </button>
                    </div>
                  </Td>
                </Tr>
              ))
            )}
          </Tbody>
        </Table>
      )}

      <ReportFormModal 
        isOpen={isFormOpen} 
        onClose={() => setIsFormOpen(false)} 
        onSubmit={createReport} 
      />

      <ReportDetailsModal 
        isOpen={!!selectedReport} 
        onClose={() => setSelectedReport(null)} 
        report={selectedReport} 
      />
    </div>
  );
};
