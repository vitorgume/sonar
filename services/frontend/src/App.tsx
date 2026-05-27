import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { ClientManagementPage } from './presentation/pages/ClientManagement/ClientManagementPage';
import { PromptManagementPage } from './presentation/pages/PromptManagement/PromptManagementPage';
import { ReportManagementPage } from './presentation/pages/ReportManagement/ReportManagementPage';
import { LoginPage } from './presentation/pages/Login/LoginPage';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/clients" element={<ClientManagementPage />} />
        <Route path="/prompts" element={<PromptManagementPage />} />
        <Route path="/reports" element={<ReportManagementPage />} />
        {/* Redirect default route to login */}
        <Route path="*" element={<Navigate to="/login" replace />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
