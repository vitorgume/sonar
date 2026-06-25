import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuthContext } from './presentation/context/AuthContext';
import { ClientManagementPage } from './presentation/pages/ClientManagement/ClientManagementPage';
import { PromptManagementPage } from './presentation/pages/PromptManagement/PromptManagementPage';
import { ReportManagementPage } from './presentation/pages/ReportManagement/ReportManagementPage';
import { LoginPage } from './presentation/pages/Login/LoginPage';
import { AppLayout } from './presentation/components/layout/AppLayout';

function AppRoutes() {
  const { isAuthenticated } = useAuthContext();

  return (
    <Routes>
      <Route
        path="/login"
        element={isAuthenticated ? <Navigate to="/reports" replace /> : <LoginPage />}
      />

      <Route
        element={isAuthenticated ? <AppLayout /> : <Navigate to="/login" replace />}
      >
        <Route path="/clients" element={<ClientManagementPage />} />
        <Route path="/prompts" element={<PromptManagementPage />} />
        <Route path="/reports" element={<ReportManagementPage />} />
      </Route>

      <Route
        path="*"
        element={<Navigate to={isAuthenticated ? '/reports' : '/login'} replace />}
      />
    </Routes>
  );
}

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <AppRoutes />
      </BrowserRouter>
    </AuthProvider>
  );
}

export default App;
