import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './presentation/context/AuthContext';
import { ClientManagementPage } from './presentation/pages/ClientManagement/ClientManagementPage';
import { PromptManagementPage } from './presentation/pages/PromptManagement/PromptManagementPage';
import { ReportManagementPage } from './presentation/pages/ReportManagement/ReportManagementPage';
import { LoginPage } from './presentation/pages/Login/LoginPage';
import { AppLayout } from './presentation/components/layout/AppLayout';

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/login" element={<LoginPage />} />
          
          {/* Authenticated Routes with Layout */}
          <Route element={<AppLayout />}>
            <Route path="/clients" element={<ClientManagementPage />} />
            <Route path="/prompts" element={<PromptManagementPage />} />
            <Route path="/reports" element={<ReportManagementPage />} />
          </Route>

          {/* Redirect default route to login */}
          <Route path="*" element={<Navigate to="/login" replace />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}

export default App;
