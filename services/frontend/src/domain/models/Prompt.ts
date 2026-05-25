import { type User } from './Client';

export interface Prompt {
  id: string;
  title: string;
  content: string;
  user?: User;
  lastUpdate: string;
}
