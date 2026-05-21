import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { ClientManagementPage } from './presentation/pages/ClientManagement/ClientManagementPage';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/clients" element={<ClientManagementPage />} />
        {/* Redirect default route to clients for now */}
        <Route path="*" element={<Navigate to="/clients" replace />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
