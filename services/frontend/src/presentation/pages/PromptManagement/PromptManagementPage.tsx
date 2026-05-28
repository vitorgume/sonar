import React, { useEffect, useState } from 'react';
import { Bot, Save, Plus, Loader2, Info } from 'lucide-react';
import { usePrompts } from '../../../application/hooks/usePrompts';
import { Button } from '../../components/ui/Button';
import { Input } from '../../components/ui/Input';

export const PromptManagementPage: React.FC = () => {
  const { prompts, loading, error, createPrompt, updatePrompt } = usePrompts();
  
  const [activePromptId, setActivePromptId] = useState<string | null>(null);
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const [isSaving, setIsSaving] = useState(false);
  const [saveMessage, setSaveMessage] = useState<{ type: 'success' | 'error', text: string } | null>(null);

  useEffect(() => {
    if (prompts.length > 0 && !activePromptId) {
      // Load the first prompt by default for the CRU flow
      const first = prompts[0];
      setActivePromptId(first.id);
      setTitle(first.title);
      setContent(first.content);
    }
  }, [prompts, activePromptId]);

  const handleSave = async () => {
    if (!title.trim() || !content.trim()) {
      setSaveMessage({ type: 'error', text: 'Título e conteúdo são obrigatórios.' });
      return;
    }

    setIsSaving(true);
    setSaveMessage(null);
    try {
      if (activePromptId) {
        // Update existing
        await updatePrompt(activePromptId, { title, content });
        setSaveMessage({ type: 'success', text: 'Prompt atualizado com sucesso!' });
      } else {
        // Create new
        const newPrompt = await createPrompt({ title, content });
        setActivePromptId(newPrompt.id);
        setSaveMessage({ type: 'success', text: 'Prompt criado com sucesso!' });
      }
    } catch (err) {
      setSaveMessage({ type: 'error', text: 'Erro ao salvar o prompt.' });
    } finally {
      setIsSaving(false);
    }
  };

  if (loading && !activePromptId && prompts.length === 0) {
    return (
      <div className="flex-1 flex items-center justify-center">
        <Loader2 className="w-8 h-8 animate-spin text-blue-600" />
      </div>
    );
  }

  return (
    <div className="flex flex-col flex-1 p-8 font-sans">
      <div className="max-w-4xl w-full mx-auto flex flex-col gap-6">
        
        {/* Header */}
        <div className="flex items-center justify-between">
          <div>
            <h1 className="text-2xl font-bold text-slate-900 flex items-center gap-2">
              <Bot className="w-6 h-6 text-purple-600" />
              Configuração de Prompt IA
            </h1>
            <p className="text-sm text-slate-600 mt-1">
              Gerencie as instruções base que guiam as análises de call da Inteligência Artificial.
            </p>
          </div>
          <Button 
            variant="primary" 
            onClick={handleSave} 
            disabled={isSaving}
          >
            {isSaving ? <Loader2 className="w-4 h-4 animate-spin" /> : (activePromptId ? <Save className="w-4 h-4" /> : <Plus className="w-4 h-4" />)}
            {activePromptId ? 'Salvar Alterações' : 'Criar Prompt'}
          </Button>
        </div>

        {error && (
          <div className="p-4 bg-red-50 border border-red-200 rounded-lg text-red-700 text-sm">
            {error}
          </div>
        )}

        {saveMessage && (
          <div className={`p-4 border rounded-lg text-sm flex items-center gap-2 ${saveMessage.type === 'success' ? 'bg-green-50 text-green-700 border-green-200' : 'bg-red-50 text-red-700 border-red-200'}`}>
            <Info className="w-4 h-4" />
            {saveMessage.text}
          </div>
        )}

        {/* Editor Card */}
        <div className="bg-white rounded-xl border border-slate-200 shadow-sm p-6 flex flex-col gap-6">
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
              className="w-full h-96 p-4 bg-slate-50 border border-slate-300 rounded-lg text-slate-900 text-sm font-mono focus:outline-none focus:ring-1 focus:ring-blue-500 focus:border-blue-500 resize-y"
              placeholder="# Contexto&#10;Você é um assistente...&#10;&#10;# Regras&#10;- Regra 1"
              value={content}
              onChange={(e) => setContent(e.target.value)}
            />
          </div>
        </div>

      </div>
    </div>
  );
};
