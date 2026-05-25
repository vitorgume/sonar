import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { ClientManagementPage } from './presentation/pages/ClientManagement/ClientManagementPage';
import { PromptManagementPage } from './presentation/pages/PromptManagement/PromptManagementPage';
import { ReportManagementPage } from './presentation/pages/ReportManagement/ReportManagementPage';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/clients" element={<ClientManagementPage />} />
        <Route path="/prompts" element={<PromptManagementPage />} />
        <Route path="/reports" element={<ReportManagementPage />} />
        {/* Redirect default route to reports for now */}
        <Route path="*" element={<Navigate to="/reports" replace />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
