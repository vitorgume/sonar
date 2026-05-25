import { type User, type Client } from './Client';

export type ReportStatus = 'PROCESSING' | 'COMPLETED';

export interface Report {
  id: string;
  title: string;
  user: User;
  client: Client;
  analysis?: string;
  transcript?: string;
  creationDate: string;
  status: ReportStatus;
}
