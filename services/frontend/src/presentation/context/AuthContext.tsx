import React, { createContext, useContext, useState, type ReactNode } from 'react';

export interface User {
  userId: string;
  name: string;
}

interface AuthContextData {
  user: User | null;
  isAuthenticated: boolean;
  signIn: (user: User) => void;
  signOut: () => void;
  loading: boolean;
}

const AuthContext = createContext<AuthContextData>({} as AuthContextData);

export const AuthProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const [loading] = useState(false); 

  // LAZY INITIALIZATION: Recupera o usuário do localStorage antes do primeiro render
  const [user, setUser] = useState<User | null>(() => {
    const storedUser = localStorage.getItem('@Sonar:user');
    if (storedUser) {
      return JSON.parse(storedUser);
    }
    return null;
  });

  const signIn = (loggedUser: User) => {
    setUser(loggedUser);
    // Salva APENAS os dados do usuário no LocalStorage (O token continua seguro no Cookie pelo backend!)
    localStorage.setItem('@Sonar:user', JSON.stringify(loggedUser));
  };

  const signOut = () => {
    setUser(null);
    localStorage.removeItem('@Sonar:user');
    window.location.href = '/login';
  };

  return (
    <AuthContext.Provider value={{ user, isAuthenticated: !!user, signIn, signOut, loading }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuthContext = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuthContext deve ser usado dentro de um AuthProvider');
  }
  return context;
};
