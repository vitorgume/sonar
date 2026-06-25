import { useState, useEffect, useCallback } from 'react';
import { type Prompt, type PromptInput } from '../../domain/models/Prompt';
import { PromptService } from '../services/PromptService';

export const usePrompts = () => {
  const [prompts, setPrompts] = useState<Prompt[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  const fetchPrompts = useCallback(async () => {
    try {
      setLoading(true);
      setError(null);
      const data = await PromptService.getAll();
      setPrompts(data);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to fetch prompts');
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchPrompts();
  }, [fetchPrompts]);

  const createPrompt = async (data: PromptInput) => {
    try {
      setLoading(true);
      setError(null);
      const newPrompt = await PromptService.create(data);
      setPrompts((prev) => [...prev, newPrompt]);
      return newPrompt;
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to create prompt');
      throw err;
    } finally {
      setLoading(false);
    }
  };

  const updatePrompt = async (id: string, data: PromptInput) => {
    try {
      setLoading(true);
      setError(null);
      const updatedPrompt = await PromptService.update(id, data);
      setPrompts((prev) => prev.map((p) => (p.id === id ? updatedPrompt : p)));
      return updatedPrompt;
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to update prompt');
      throw err;
    } finally {
      setLoading(false);
    }
  };

  return {
    prompts,
    loading,
    error,
    fetchPrompts,
    createPrompt,
    updatePrompt,
  };
};
