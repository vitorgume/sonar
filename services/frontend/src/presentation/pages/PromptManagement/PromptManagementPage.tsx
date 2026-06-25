import React, { useEffect, useMemo, useState } from 'react';
import { Bot, Save, Plus, Loader2, Info } from 'lucide-react';
import { usePrompts } from '../../../application/hooks/usePrompts';
import { useClients } from '../../../application/hooks/useClients';
import { Button } from '../../components/ui/Button';
import { Input } from '../../components/ui/Input';

export const PromptManagementPage: React.FC = () => {
  const { prompts, loading, error, createPrompt, updatePrompt } = usePrompts();
  const { clients, isLoading: clientsLoading, error: clientsError } = useClients();
  const [selectedClientId, setSelectedClientId] = useState('');
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const [isSaving, setIsSaving] = useState(false);
  const [saveMessage, setSaveMessage] = useState<{ type: 'success' | 'error', text: string } | null>(null);
  const selectedPrompt = useMemo(
    () => prompts.find((prompt) => prompt.client?.id === selectedClientId) ?? null,
    [prompts, selectedClientId]
  );

  useEffect(() => {
    if (!selectedClientId && clients.length > 0) {
      setSelectedClientId(clients[0].id);
      return;
    }

    if (clients.length === 0 && selectedClientId) {
      setSelectedClientId('');
    }
  }, [clients, selectedClientId]);

  useEffect(() => {
    if (!selectedClientId) {
      setTitle('');
      setContent('');
      return;
    }

    if (selectedPrompt) {
      setTitle(selectedPrompt.title);
      setContent(selectedPrompt.content);
    } else {
      setTitle('');
      setContent('');
    }
  }, [selectedClientId, selectedPrompt]);

  const handleSave = async () => {
    if (!selectedClientId) {
      setSaveMessage({ type: 'error', text: 'Selecione um cliente para configurar o prompt.' });
      return;
    }

    if (!title.trim() || !content.trim()) {
      setSaveMessage({ type: 'error', text: 'Título e conteúdo são obrigatórios.' });
      return;
    }

    setIsSaving(true);
    setSaveMessage(null);
    try {
      const payload = {
        title,
        content,
        client: {
          id: selectedClientId,
        },
      };

      if (selectedPrompt) {
        await updatePrompt(selectedPrompt.id, {
          ...payload,
        });
        setSaveMessage({ type: 'success', text: 'Prompt atualizado com sucesso!' });
      } else {
        await createPrompt(payload);
        setSaveMessage({ type: 'success', text: 'Prompt criado com sucesso para o cliente selecionado!' });
      }
    } catch (err) {
      setSaveMessage({ type: 'error', text: 'Erro ao salvar o prompt.' });
    } finally {
      setIsSaving(false);
    }
  };

  const isInitialLoading = loading || clientsLoading;
  const combinedError = error || clientsError;
  const hasClients = clients.length > 0;
  const isSaveDisabled = isSaving || !selectedClientId || !title.trim() || !content.trim();

  if (isInitialLoading && !hasClients && prompts.length === 0) {
    return (
      <div className="flex-1 flex items-center justify-center">
        <Loader2 className="w-8 h-8 animate-spin text-blue-600" />
      </div>
    );
  }

  return (
    <div className="flex flex-col flex-1 p-8 font-sans">
      <div className="max-w-4xl w-full mx-auto flex flex-col gap-6">
        <div className="flex items-center justify-between">
          <div>
            <h1 className="text-2xl font-bold text-slate-900 flex items-center gap-2">
              <Bot className="w-6 h-6 text-purple-600" />
              Configuração de Prompt IA
            </h1>
            <p className="text-sm text-slate-600 mt-1">
              Gerencie as instruções da IA por cliente. Cada cliente possui seu próprio prompt.
            </p>
          </div>
          <Button
            variant="primary"
            onClick={handleSave}
            disabled={isSaveDisabled}
          >
            {isSaving ? <Loader2 className="w-4 h-4 animate-spin" /> : (selectedPrompt ? <Save className="w-4 h-4" /> : <Plus className="w-4 h-4" />)}
            {selectedPrompt ? 'Salvar Alterações' : 'Criar Prompt'}
          </Button>
        </div>

        {combinedError && (
          <div className="p-4 bg-red-50 border border-red-200 rounded-lg text-red-700 text-sm">
            {combinedError}
          </div>
        )}

        {saveMessage && (
          <div className={`p-4 border rounded-lg text-sm flex items-center gap-2 ${saveMessage.type === 'success' ? 'bg-green-50 text-green-700 border-green-200' : 'bg-red-50 text-red-700 border-red-200'}`}>
            <Info className="w-4 h-4" />
            {saveMessage.text}
          </div>
        )}

        {!hasClients && (
          <div className="p-4 bg-amber-50 border border-amber-200 rounded-lg text-amber-800 text-sm">
            Cadastre pelo menos um cliente antes de configurar prompts da IA.
          </div>
        )}

        <div className="bg-white rounded-xl border border-slate-200 shadow-sm p-6 flex flex-col gap-6">
          <div className="flex flex-col gap-2">
            <label className="text-sm font-semibold text-slate-900">
              Cliente
            </label>
            <select
              className="w-full rounded-lg border border-slate-300 bg-slate-50 px-4 py-3 text-sm text-slate-900 focus:outline-none focus:ring-1 focus:ring-blue-500 focus:border-blue-500 disabled:cursor-not-allowed disabled:bg-slate-100"
              value={selectedClientId}
              onChange={(e) => {
                setSelectedClientId(e.target.value);
                setSaveMessage(null);
              }}
              disabled={!hasClients || isSaving}
            >
              <option value="">Selecione um cliente</option>
              {clients.map((client) => (
                <option key={client.id} value={client.id}>
                  {client.name}
                </option>
              ))}
            </select>
            <p className="text-xs text-slate-500">
              O prompt salvo aqui sera usado nas analises dos relatórios desse cliente.
            </p>
          </div>

          {selectedClientId && !selectedPrompt && (
            <div className="p-4 bg-purple-50 border border-purple-200 rounded-lg text-purple-800 text-sm flex items-center gap-2">
              <Info className="w-4 h-4" />
              Nenhum prompt encontrado para este cliente. Preencha os campos abaixo para criar o primeiro.
            </div>
          )}

          <Input
            label="Título do Prompt"
            placeholder="Ex: Prompt Principal de Análise de Vendas"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
          />

          <div className="flex flex-col gap-2">
            <div className="flex items-center justify-between">
              <label className="text-sm font-semibold text-slate-900">
                Conteúdo (Suporta Markdown)
              </label>
              <span className="text-xs text-slate-500 bg-slate-100 px-2 py-1 rounded">
                Markdown Enabled
              </span>
            </div>

            <textarea
              className="w-full h-96 p-4 bg-slate-50 border border-slate-300 rounded-lg text-slate-900 text-sm font-mono focus:outline-none focus:ring-1 focus:ring-blue-500 focus:border-blue-500 resize-y disabled:cursor-not-allowed disabled:bg-slate-100"
              placeholder="# Contexto&#10;Você é um assistente...&#10;&#10;# Regras&#10;- Regra 1"
              value={content}
              onChange={(e) => setContent(e.target.value)}
              disabled={!selectedClientId}
            />
          </div>

          {selectedPrompt?.lastUpdate && (
            <p className="text-xs text-slate-500">
              Última atualização: {new Date(selectedPrompt.lastUpdate).toLocaleString()}
            </p>
          )}
        </div>
      </div>
    </div>
  );
};
