import React from 'react';
import { NavLink } from 'react-router-dom';
import { Users, FileText, Bot, LogOut, LayoutDashboard } from 'lucide-react';
import { useAuthContext } from '../../context/AuthContext';

export const Sidebar: React.FC = () => {
  const { signOut } = useAuthContext();

  const handleLogout = () => {
    signOut();
  };

  const navItems = [
    { to: '/reports', icon: FileText, label: 'Relatórios' },
    { to: '/clients', icon: Users, label: 'Clientes' },
    { to: '/prompts', icon: Bot, label: 'Prompts' },
  ];

  return (
    <aside className="w-64 bg-slate-900 text-white flex flex-col min-h-screen">
      <div className="p-6 flex items-center gap-3 border-b border-slate-800">
        <div className="bg-blue-600 p-2 rounded-lg">
          <LayoutDashboard className="w-6 h-6 text-white" />
        </div>
        <span className="text-xl font-bold tracking-wide">Sonar</span>
      </div>
      
      <nav className="flex-1 py-6 flex flex-col gap-2 px-4">
        {navItems.map((item) => (
          <NavLink
            key={item.to}
            to={item.to}
            className={({ isActive }) =>
              `flex items-center gap-3 px-4 py-3 rounded-lg transition-colors ${
                isActive
                  ? 'bg-blue-600 text-white shadow-sm'
                  : 'text-slate-400 hover:bg-slate-800 hover:text-slate-200'
              }`
            }
          >
            <item.icon className="w-5 h-5" />
            <span className="font-medium">{item.label}</span>
          </NavLink>
        ))}
      </nav>

      <div className="p-4 border-t border-slate-800">
        <button
          onClick={handleLogout}
          className="flex items-center gap-3 px-4 py-3 w-full rounded-lg text-slate-400 hover:bg-slate-800 hover:text-slate-200 transition-colors"
        >
          <LogOut className="w-5 h-5" />
          <span className="font-medium">Sair</span>
        </button>
      </div>
    </aside>
  );
};
