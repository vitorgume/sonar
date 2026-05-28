import { type Report } from '../../domain/models/Report';

interface ApiResponse<T> {
  dado: T;
  erro: { mensagens: string[] } | null;
}


export const ReportService = {
  getAll: async (): Promise<Report[]> => {
    const response = await fetch(` /reports`);
    if (!response.ok) throw new Error('Failed to fetch reports');
    const result: ApiResponse<Report[]> = await response.json();
    return result.dado;
  },

  create: async (data: { title: string; clientId: string; audioFileKey: string }): Promise<Report> => {
    const payload = {
      title: data.title,
      client: { id: data.clientId },
      transcript: '',
      audioFileKey: data.audioFileKey,
      // Backend expects a UserDto if it doesn't resolve from context.
      // Passing a mocked user object to satisfy typical dto requirements
      user: { id: '00000000-0000-0000-0000-000000000000', name: 'Current User' } 
    };

    const response = await fetch(` /reports`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload),
    });

    if (!response.ok) throw new Error('Failed to create report');
    const result: ApiResponse<Report> = await response.json();
    return result.dado;
  },

  delete: async (id: string): Promise<void> => {
    const response = await fetch(` /reports/${id}`, {
      method: 'DELETE',
    });
    if (!response.ok) throw new Error('Failed to delete report');
  }
};
