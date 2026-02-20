import React, { useState } from 'react';
import Navbar from '../components/Navbar';
import LoginPage from '../components/LoginPage';
import Dashboard from '../components/Dashboard';
import Catalog from '../components/Catalog';
import Orders from '../components/Orders';
import UserManagement from '../components/UserManagement';
import ClientManagement from '../components/ClientManagement';
import LivreurManagement from '../components/LivreurManagement';
import Deliveries from '../components/Deliveries';
import Complaints from '../components/Complaints';
import Support from '../components/Support';
import Toast from '../components/Toast';
import { CartProvider } from '../contexts/CartContext';

const Index = () => {
  const [currentRoute, setCurrentRoute] = useState('login');
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [toast, setToast] = useState<{ message: string, type: 'success' | 'error' | 'info' } | null>(null);

  const showToast = (message: string, type: 'success' | 'error' | 'info') => {
    setToast({ message, type });
  };

  const closeToast = () => {
    setToast(null);
  };

  const handleNavigate = (route: string) => {
    if (route === 'login') {
      setIsLoggedIn(false);
      setCurrentRoute('login');
    } else {
      setCurrentRoute(route);
    }
  };

  const handleLogin = () => {
    setIsLoggedIn(true);
    setCurrentRoute('dashboard');
  };

  const renderContent = () => {
    if (!isLoggedIn && currentRoute !== 'login') {
      return <LoginPage onLogin={handleLogin} onNavigate={handleNavigate} showToast={showToast} />;
    }

    switch (currentRoute) {
      case 'login':
        return <LoginPage onLogin={handleLogin} onNavigate={handleNavigate} showToast={showToast} />;
      case 'dashboard':
        return <Dashboard />;
      case 'catalog':
        return <Catalog showToast={showToast} />;
      case 'orders':
        return <Orders showToast={showToast} />;
      case 'users':
        return <UserManagement showToast={showToast} />;
      case 'clients':
        return <ClientManagement showToast={showToast} />;
      case 'livreurs':
        return <LivreurManagement showToast={showToast} />;
      case 'deliveries':
        return <Deliveries showToast={showToast} />;
      case 'complaints':
        return <Complaints showToast={showToast} />;
      case 'support':
        return <Support showToast={showToast} />;
      default:
        return <UserManagement showToast={showToast} />;
    }
  };

  return (
    <CartProvider>
      <div className="min-h-screen bg-gray-50">
        {isLoggedIn && (
          <Navbar activeRoute={currentRoute} onNavigate={handleNavigate} isLoggedIn={isLoggedIn} />
        )}
        {renderContent()}
        {toast && (
          <Toast
            message={toast.message}
            type={toast.type}
            onClose={closeToast}
          />
        )}
      </div>
    </CartProvider>
  );
};

export default Index;