import React from 'react';
import { useState } from 'react';
import { Home, Package, FileText, Users, Truck, MessageSquare, HelpCircle, LogOut, Menu, X, LogOut as LogOutIcon } from 'lucide-react';

interface NavbarProps {
  activeRoute: string;
  onNavigate: (route: string) => void;
  isLoggedIn?: boolean;
}

const Navbar = ({ activeRoute, onNavigate, isLoggedIn }: NavbarProps) => {
  const [isMenuOpen, setIsMenuOpen] = useState(false);

  const rolesString = localStorage.getItem('roles') || '';
  const userRoles = rolesString.split(',').map(role => role.trim()).filter(role => role);

  const navItems = [
    { route: 'dashboard', label: 'Dashboard', icon: Home, roles: ['ROLE_ADMIN', 'ROLE_CLIENT', 'ROLE_LIVREUR'] },
    { route: 'catalog', label: 'Catalogue', icon: Package, roles: ['ROLE_ADMIN', 'ROLE_CLIENT'] },
    { route: 'orders', label: 'Commandes', icon: FileText, roles: ['ROLE_ADMIN', 'ROLE_CLIENT'] },
    { route: 'users', label: 'Utilisateurs', icon: Users, roles: ['ROLE_ADMIN'] },
    { route: 'clients', label: 'Clients', icon: Users, roles: ['ROLE_ADMIN'] },
    { route: 'livreurs', label: 'Livreurs', icon: Truck, roles: ['ROLE_ADMIN'] },
    { route: 'deliveries', label: 'Livraisons', icon: Truck, roles: ['ROLE_ADMIN', 'ROLE_LIVREUR'] },
    { route: 'complaints', label: 'Réclamations', icon: MessageSquare, roles: ['ROLE_ADMIN', 'ROLE_CLIENT'] },
    { route: 'support', label: 'Support', icon: HelpCircle, roles: ['ROLE_ADMIN', 'ROLE_CLIENT', 'ROLE_LIVREUR'] },
    { route: 'logout', label: 'Déconnexion', icon: LogOutIcon, roles: ['ROLE_ADMIN', 'ROLE_CLIENT', 'ROLE_LIVREUR'] },
  ];

  const filteredItems = isLoggedIn
    ? navItems.filter(item => userRoles.some(role => item.roles.includes(role)))
    : [];

  return (
    <nav className="bg-primary border-b border-border shadow-md h-14">
      <div className="max-w-7xl mx-auto px-2 sm:px-4 h-full flex items-center">
        <div className="flex-shrink-0 flex items-center space-x-4">
          <button
            onClick={() => onNavigate('dashboard')}
            className="focus:outline-none focus:ring-2 focus:ring-secondary focus:ring-offset-2 rounded-md"
            aria-label="Accueil"
          >
            <img
              src="/az.jpg"
              alt="Logo Afriquia Gaz"
              className="h-11 w-auto object-contain border border-secondary/30 rounded-md"
            />
          </button>
          <span className="text-primary-foreground text-xl font-bold tracking-tight">Allo Gaz</span>
        </div>
        <div className="flex-1 flex justify-end">
          <div className="sm:hidden">
            <button
              onClick={() => setIsMenuOpen(!isMenuOpen)}
              className="text-primary-foreground focus:outline-none focus:ring-2 focus:ring-secondary p-2 rounded-md"
              aria-label={isMenuOpen ? 'Fermer le menu' : 'Ouvrir le menu'}
            >
              {isMenuOpen ? <X size={20} /> : <Menu size={20} />}
            </button>
          </div>
          <div className={`sm:flex sm:items-center sm:space-x-2 ${isMenuOpen ? 'block' : 'hidden'} sm:block absolute sm:static top-14 left-0 right-0 bg-primary sm:bg-transparent p-4 sm:p-2 flex-col sm:flex-row w-full sm:w-auto`}>
            {filteredItems.length > 0 ? (
              filteredItems.map((item) => (
                <button
                  key={item.route}
                  onClick={() => {
                    if (item.route === 'logout') {
                      localStorage.clear();
                      onNavigate('login');
                    } else {
                      onNavigate(item.route);
                    }
                    if (isMenuOpen) setIsMenuOpen(false);
                  }}
                  className={`inline-flex items-center px-3 py-2 text-sm font-medium rounded-md ${
                    activeRoute === item.route
                      ? 'bg-secondary text-secondary-foreground'
                      : 'text-primary-foreground hover:bg-secondary/30 hover:text-secondary-foreground'
                  } transition-colors duration-200 w-full sm:w-auto text-left sm:text-center mb-2 sm:mb-0`}
                >
                  <item.icon className="mr-2 h-5 w-5" strokeWidth={1.5} />
                  {item.label}
                </button>
              ))
            ) : (
              <p className="text-primary-foreground text-sm p-2">Aucun menu disponible</p>
            )}
          </div>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;