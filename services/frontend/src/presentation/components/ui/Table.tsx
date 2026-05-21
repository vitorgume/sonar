import type { ReactNode } from 'react';

export const Table = ({ children }: { children: ReactNode }) => (
  <div className="w-full overflow-x-auto rounded-xl border border-slate-200 shadow-sm bg-white">
    <table className="w-full text-left text-sm text-slate-600">
      {children}
    </table>
  </div>
);

export const Thead = ({ children }: { children: ReactNode }) => (
  <thead className="bg-slate-50 text-slate-900 border-b border-slate-200">
    {children}
  </thead>
);

export const Tbody = ({ children }: { children: ReactNode }) => (
  <tbody className="divide-y divide-slate-100">
    {children}
  </tbody>
);

export const Tr = ({ children, className = '' }: { children: ReactNode; className?: string }) => (
  <tr className={`hover:bg-slate-50 transition-colors ${className}`}>
    {children}
  </tr>
);

export const Th = ({ children, className = '' }: { children: ReactNode; className?: string }) => (
  <th className={`px-6 py-4 font-semibold ${className}`}>
    {children}
  </th>
);

export const Td = ({ children, className = '', colSpan }: { children: ReactNode; className?: string; colSpan?: number }) => (
  <td className={`px-6 py-4 ${className}`} colSpan={colSpan}>
    {children}
  </td>
);
