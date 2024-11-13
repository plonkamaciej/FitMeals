import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { LoginForm } from './components/LoginForm';
import { RegisterForm } from './components/RegisterForm';
import DiaryView  from './components/DiaryView'; // Twój główny komponent z dziennikiem
import Settings from './components/Settings';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/login" element={<LoginForm />} />
        <Route path="/register" element={<RegisterForm />} />
        <Route 
          path="/diary" 
          element={
            <ProtectedRoute>
              <DiaryView />
            </ProtectedRoute>
          } 
        />
         <Route
          path="/settings"
          element={
            <ProtectedRoute>
              <Settings userId={0} />
            </ProtectedRoute>
          }
        />
        <Route path="/" element={<Navigate to="/login" replace />} />
      </Routes>
    </Router>
  );
}

// Komponent do chronienia tras
function ProtectedRoute({ children }: { children: React.ReactNode }) {
  const token = localStorage.getItem('token');
  
  if (!token) {
    return <Navigate to="/login" replace />;
  }

  return <>{children}</>;
}

export default App;
