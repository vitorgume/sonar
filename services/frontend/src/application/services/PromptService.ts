import type { Prompt, PromptInput } from '../../domain/models/Prompt';
import { API_BASE_URL } from '../config/env';

interface ApiResponse<T> {
  dado: T;
  erro: { mensagens: string[] } | null;
}

export const PromptService = {
  getAll: async (): Promise<Prompt[]> => {
    const response = await fetch(`${API_BASE_URL}/prompts`, {
      credentials: 'include' // <-- Adicionado
    });
    if (!response.ok) throw new Error('Failed to fetch prompts');
    const result: ApiResponse<Prompt[]> = await response.json();
    return result.dado;
  },

  getById: async (id: string): Promise<Prompt | undefined> => {
    const response = await fetch(`${API_BASE_URL}/prompts/${id}`, {
      credentials: 'include' // <-- Adicionado
    });
    if (!response.ok) throw new Error('Failed to fetch prompt');
    const result: ApiResponse<Prompt> = await response.json();
    return result.dado;
  },

  create: async (data: PromptInput): Promise<Prompt> => {
    const response = await fetch(`${API_BASE_URL}/prompts`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      credentials: 'include', // <-- Adicionado
      body: JSON.stringify(data),
    });
    if (!response.ok) throw new Error('Failed to create prompt');
    const result: ApiResponse<Prompt> = await response.json();
    return result.dado;
  },

  update: async (id: string, data: PromptInput): Promise<Prompt> => {
    const response = await fetch(`${API_BASE_URL}/prompts/${id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      credentials: 'include', // <-- Adicionado
      body: JSON.stringify(data),
    });
    if (!response.ok) throw new Error('Failed to update prompt');
    const result: ApiResponse<Prompt> = await response.json();
    return result.dado;
  }
};
