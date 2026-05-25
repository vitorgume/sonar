import { type Report } from '../../domain/models/Report';

// Initial Mock Data
let mockReports: Report[] = [
  {
    id: '1',
    title: 'Q1 Sales Call with Acme Corp',
    user: { id: 'u1', name: 'John Doe' },
    client: { id: 'c1', name: 'Acme Corp', user: { id: 'u1', name: 'John Doe' }, creationDate: new Date().toISOString() },
    analysis: 'Positive sentiment overall. The client is interested in the enterprise plan. They highlighted some concerns regarding implementation time.',
    transcript: 'John: Hello, thanks for joining.\nClient: Hi John, let\'s get straight to the pricing.\nJohn: Sure, we have a few options...',
    creationDate: new Date(Date.now() - 86400000).toISOString(),
    status: 'COMPLETED',
  },
  {
    id: '2',
    title: 'Initial Sync - Globex',
    user: { id: 'u1', name: 'John Doe' },
    client: { id: 'c2', name: 'Globex', user: { id: 'u1', name: 'John Doe' }, creationDate: new Date().toISOString() },
    analysis: '',
    transcript: '',
    creationDate: new Date().toISOString(),
    status: 'PROCESSING',
  }
];

export const ReportService = {
  getAll: async (): Promise<Report[]> => {
    return new Promise((resolve) => {
      setTimeout(() => resolve([...mockReports]), 500);
    });
  },

  create: async (data: { title: string; clientId: string; transcript: string }): Promise<Report> => {
    return new Promise((resolve) => {
      const newReport: Report = {
        id: crypto.randomUUID(),
        title: data.title,
        user: { id: 'u1', name: 'Current User' }, // Mocking user
        client: { id: data.clientId, name: `Client ${data.clientId}`, user: { id: 'u1', name: 'Current User' }, creationDate: new Date().toISOString() }, // Mocking client
        transcript: data.transcript,
        analysis: '', // Analysis will be generated eventually
        creationDate: new Date().toISOString(),
        status: 'PROCESSING',
      };
      mockReports = [...mockReports, newReport];
      setTimeout(() => resolve(newReport), 500);
    });
  },

  delete: async (id: string): Promise<void> => {
    return new Promise((resolve) => {
      mockReports = mockReports.filter((r) => r.id !== id);
      setTimeout(() => resolve(), 300);
    });
  }
};
