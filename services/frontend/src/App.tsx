import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { ClientManagementPage } from './presentation/pages/ClientManagement/ClientManagementPage';
import { PromptManagementPage } from './presentation/pages/PromptManagement/PromptManagementPage';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/clients" element={<ClientManagementPage />} />
        <Route path="/prompts" element={<PromptManagementPage />} />
        {/* Redirect default route to clients for now */}
        <Route path="*" element={<Navigate to="/prompts" replace />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
