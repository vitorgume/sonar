export interface User {
  id: string;
  name: string;
  email?: string;
}

export interface Client {
  id: string;
  name: string;
  user: User;
  creationDate: string;
}
