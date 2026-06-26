import { type Client } from './Client';

export interface Prompt {
  id: string;
  title: string;
  content: string;
  client: Client;
  lastUpdate: string;
}

export interface PromptInput {
  title: string;
  content: string;
  client: Pick<Client, 'id'>;
}
