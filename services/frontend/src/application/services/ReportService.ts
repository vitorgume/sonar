import { type Report } from '../../domain/models/Report';
import { API_BASE_URL } from '../config/env';

interface ApiResponse<T> {
  dado: T;
  erro: { mensagens: string[] } | null;
}

export const ReportService = {
  // ... (o método getAll continua igual)

  // Agora exigimos o userId e userName nos parâmetros!
  create: async (data: { title: string; clientId: string; audioFileKey: string; userId: string; userName: string }): Promise<Report> => {
    const payload = {
      title: data.title,
      client: { id: data.clientId },
      transcript: '',
      audioFileKey: data.audioFileKey,
      user: { id: data.userId, name: data.userName } // <-- Agora usamos o real!
    };

    const response = await fetch(`${API_BASE_URL}/reports`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      credentials: 'include',
      body: JSON.stringify(payload),
    });

    if (!response.ok) throw new Error('Failed to create report');
    const result: ApiResponse<Report> = await response.json();
    return result.dado;
  },

  delete: async (id: string): Promise<void> => {
    const response = await fetch(`${API_BASE_URL}/reports/${id}`, {
      method: 'DELETE',
      credentials: 'include', 
      headers: { 'Content-Type': 'application/json' }
    });
    if (!response.ok) throw new Error('Failed to delete report');
  },

  getAll: async (): Promise<Report[]> => {
    const response = await fetch(`${API_BASE_URL}/reports`, {
      credentials: 'include', 
      headers: { 'Content-Type': 'application/json' }
    });
    if (!response.ok) throw new Error('Failed to fetch reports');
    const result: ApiResponse<Report[]> = await response.json();
    return result.dado;
  },
};