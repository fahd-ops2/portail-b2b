import { useState, useEffect } from 'react';
import { User, Edit, Trash2 } from 'lucide-react';
import { API_BASE_URL } from '@/config/api';
import { fetchLivreurs } from '../service/livreurService';

interface UserManagementProps {
  showToast: (message: string, type: 'success' | 'error' | 'info') => void;
}

interface User {
  id: number;
  nom: string;
  email: string;
  phone: string;
  available?: boolean;
  role: 'ROLE_LIVREUR' | 'ROLE_CLIENT';
}

const UserManagement = ({ showToast }: UserManagementProps) => {
  const [users, setUsers] = useState<User[]>([]);
  const [showForm, setShowForm] = useState(false);
  const [editingUser, setEditingUser] = useState<User | null>(null);
  const [formData, setFormData] = useState({
    nom: '',
    email: '',
    phone: '',
    password: '',
    available: true,
    role: 'ROLE_LIVREUR' as 'ROLE_LIVREUR' | 'ROLE_CLIENT',
  });
  const [isLoading, setIsLoading] = useState(false);
  const [filterRole, setFilterRole] = useState<'ALL' | 'ROLE_LIVREUR' | 'ROLE_CLIENT'>('ALL');
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(1);

  useEffect(() => {
    const fetchUsers = async () => {
      setIsLoading(true);
      try {
        const token = localStorage.getItem('authToken');
        let livreurs: User[] = [];
        let clients: User[] = [];

        if (filterRole === 'ALL' || filterRole === 'ROLE_LIVREUR') {
          const livreurResponse = await fetchLivreurs();
          if (!Array.isArray(livreurResponse)) {
            console.error('Erreur: réponse livreurs non tableau:', livreurResponse);
            showToast('La réponse de l\'API livreurs n\'est pas au format attendu', 'error');
          } else {
            livreurs = livreurResponse;
          }
        }

        if (filterRole === 'ALL' || filterRole === 'ROLE_CLIENT') {
          const clientResponse = await fetch(`${API_BASE_URL}/clients?size=10&page=0`, {
            method: 'GET',
            headers: {
              'Content-Type': 'application/json',
              'Authorization': `Bearer ${token}`
            }
          });
          const clientText = await clientResponse.text();
          const clientData = clientText ? JSON.parse(clientText) : { content: [], totalPages: 1 };
          if (!Array.isArray(clientData.content)) {
            console.error('Erreur: réponse clients.content non tableau:', clientData.content);
            showToast('La réponse de l\'API clients n\'est pas au format attendu', 'error');
          } else {
            clients = clientData.content;
            setTotalPages(clientData.totalPages || 1);
          }
        }

        const combinedUsers = [...livreurs, ...clients];
        setUsers(combinedUsers);
      } catch (error) {
        console.error('Erreur fetchUsers:', error.message);
        showToast(`Erreur lors de la récupération des utilisateurs: ${error.message}`, 'error');
        setUsers([]);
      } finally {
        setIsLoading(false);
      }
    };
    fetchUsers();
  }, [filterRole, currentPage]);

  const handleAddUser = async () => {
    const token = localStorage.getItem('authToken');
    if (!token) {
      showToast('Veuillez vous connecter pour voir l\'historique', 'error');
      return;
    }

    if (!formData.nom || !formData.email || !formData.phone || !formData.password) {
      showToast('Veuillez remplir tous les champs', 'error');
      return;
    }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(formData.email)) {
      showToast('Adresse e-mail invalide', 'error');
      return;
    }

    const phoneRegex = /^\+?\d{10,15}$/;
    if (!phoneRegex.test(formData.phone)) {
      showToast('Numéro de téléphone invalide', 'error');
      return;
    }

    if (formData.password.length < 6) {
      showToast('Le mot de passe doit contenir au moins 6 caractères', 'error');
      return;
    }

    setIsLoading(true);
    try {
      const endpoint = formData.role === 'ROLE_LIVREUR' ? 'livreurs' : 'clients';
      const response = await fetch(`${API_BASE_URL}/${endpoint}`, {
        method: 'POST',
        headers: { 
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json' },
        body: JSON.stringify(formData),
      });
      if (!response.ok) {
        throw new Error(`Erreur HTTP: ${response.status} ${response.statusText}`);
      }
      const newUser = await response.json();
      setUsers([...users, newUser]);
      setFormData({
        nom: '',
        email: '',
        phone: '',
        password: '',
        available: true,
        role: 'ROLE_LIVREUR',
      });
      setShowForm(false);
      showToast('Utilisateur ajouté avec succès', 'success');
    } catch (error) {
      console.error('Erreur handleAddUser:', error.message);
      showToast(`Erreur lors de l'ajout de l'utilisateur: ${error.message}`, 'error');
    } finally {
      setIsLoading(false);
    }
  };

  const handleEditUser = (user: User) => {
    setEditingUser(user);
    setFormData({
      nom: user.nom,
      email: user.email,
      phone: user.phone,
      password: '',
      available: user.available ?? true,
      role: user.role,
    });
    setShowForm(true);
  };

  const handleUpdateUser = async () => {
    if (!editingUser) return;

    if (!formData.nom || !formData.email || !formData.phone) {
      showToast('Veuillez remplir tous les champs', 'error');
      return;
    }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(formData.email)) {
      showToast('Adresse e-mail invalide', 'error');
      return;
    }

    const phoneRegex = /^\+?\d{10,15}$/;
    if (!phoneRegex.test(formData.phone)) {
      showToast('Numéro de téléphone invalide', 'error');
      return;
    }

    setIsLoading(true);
    try {
      const endpoint = editingUser.role === 'ROLE_LIVREUR' ? 'livreurs' : 'clients';
      const response = await fetch(`${API_BASE_URL}/${endpoint}/${editingUser.id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(formData),
      });
      if (!response.ok) {
        throw new Error(`Erreur HTTP: ${response.status} ${response.statusText}`);
      }
      const updatedUser = await response.json();
      setUsers(users.map(user => (user.id === editingUser.id ? updatedUser : user)));
      setFormData({
        nom: '',
        email: '',
        phone: '',
        password: '',
        available: true,
        role: 'ROLE_LIVREUR',
      });
      setShowForm(false);
      setEditingUser(null);
      showToast('Utilisateur modifié avec succès', 'success');
    } catch (error) {
      console.error('Erreur handleUpdateUser:', error.message);
      showToast(`Erreur lors de la mise à jour de l'utilisateur: ${error.message}`, 'error');
    } finally {
      setIsLoading(false);
    }
  };

  const handleDeleteUser = async (userId: number, role: 'ROLE_LIVREUR' | 'ROLE_CLIENT') => {
    setIsLoading(true);
    try {
      const endpoint = role === 'ROLE_LIVREUR' ? 'livreurs' : 'clients';
      const response = await fetch(`${API_BASE_URL}/${endpoint}/${userId}`, {
        method: 'DELETE',
      });
      if (!response.ok) {
        throw new Error(`Erreur HTTP: ${response.status} ${response.statusText}`);
      }
      setUsers(users.filter(user => user.id !== userId));
      showToast('Utilisateur supprimé', 'success');
    } catch (error) {
      console.error('Erreur handleDeleteUser:', error.message);
      showToast(`Erreur lors de la suppression de l'utilisateur: ${error.message}`, 'error');
    } finally {
      setIsLoading(false);
    }
  };

  const handleToggleAvailability = async (userId: number) => {
    setIsLoading(true);
    try {
      const response = await fetch(`${API_BASE_URL}/livreurs/${userId}/toggle`, {
        method: 'PATCH',
        headers: { 'Content-Type': 'application/json' },
      });
      if (!response.ok) {
        throw new Error(`Erreur HTTP: ${response.status} ${response.statusText}`);
      }
      const updatedUser = await response.json();
      setUsers(users.map(user => (user.id === userId ? updatedUser : user)));
      showToast('Disponibilité de l\'utilisateur mise à jour', 'success');
    } catch (error) {
      console.error('Erreur handleToggleAvailability:', error.message);
      showToast(`Erreur lors de la mise à jour de la disponibilité: ${error.message}`, 'error');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="bg-gray-900 min-h-screen font-afriquia px-2 sm:px-4">
      <div className="max-w-7xl mx-auto py-6">
        <div className="flex justify-between items-center mb-6">
          <h1 className="text-2xl sm:text-3xl font-bold text-white tracking-tight">Gestion des Utilisateurs</h1>
          <div className="flex gap-2 sm:gap-4">
            <select
              value={filterRole}
              onChange={(e) => setFilterRole(e.target.value as 'ALL' | 'ROLE_LIVREUR' | 'ROLE_CLIENT')}
              className="px-3 py-2 border border-gray-700 rounded-md focus:outline-none focus:ring-2 focus:ring-afriquia-orange text-white bg-gray-800 hover:bg-gray-700 transition-colors duration-200 text-sm sm:text-base"
            >
              <option value="ALL">Tous</option>
              <option value="ROLE_LIVREUR">Livreurs</option>
              <option value="ROLE_CLIENT">Clients</option>
            </select>
            <button
              onClick={() => {
                setShowForm(!showForm);
                setEditingUser(null);
                setFormData({
                  nom: '',
                  email: '',
                  phone: '',
                  password: '',
                  available: true,
                  role: 'ROLE_LIVREUR',
                });
              }}
              className="inline-flex items-center px-3 py-2 sm:px-4 sm:py-2 bg-afriquia-orange text-white rounded-md hover:bg-afriquia-orange/80 transition-colors duration-200 text-sm sm:text-base disabled:opacity-50 shadow-afriquia"
              disabled={isLoading}
            >
              <User className="mr-2 h-4 w-4 sm:h-5 sm:w-5" strokeWidth={1.5} />
              Ajouter un utilisateur
            </button>
          </div>
        </div>

        {showForm && (
          <div className="bg-gray-800 rounded-md shadow-afriquia border border-gray-700 p-4 sm:p-6 mb-6">
            <h2 className="text-xl sm:text-2xl font-semibold text-white mb-4 sm:mb-6">
              {editingUser ? 'Modifier l\'utilisateur' : 'Ajouter un utilisateur'}
            </h2>
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 sm:gap-6">
              <div className="sm:col-span-2">
                <label className="block text-sm font-medium text-gray-200 mb-2">Nom *</label>
                <input
                  type="text"
                  value={formData.nom}
                  onChange={(e) => setFormData({ ...formData, nom: e.target.value })}
                  className="w-full px-3 py-2 border border-gray-700 rounded-md focus:outline-none focus:ring-2 focus:ring-afriquia-orange text-white bg-gray-900 hover:bg-gray-800 transition-colors duration-200"
                  placeholder="Nom de l'utilisateur"
                  disabled={isLoading}
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-200 mb-2">Email *</label>
                <input
                  type="email"
                  value={formData.email}
                  onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                  className="w-full px-3 py-2 border border-gray-700 rounded-md focus:outline-none focus:ring-2 focus:ring-afriquia-orange text-white bg-gray-900 hover:bg-gray-800 transition-colors duration-200"
                  placeholder="contact@entreprise.com"
                  disabled={isLoading}
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-200 mb-2">Téléphone *</label>
                <input
                  type="tel"
                  value={formData.phone}
                  onChange={(e) => setFormData({ ...formData, phone: e.target.value })}
                  className="w-full px-3 py-2 border border-gray-700 rounded-md focus:outline-none focus:ring-2 focus:ring-afriquia-orange text-white bg-gray-900 hover:bg-gray-800 transition-colors duration-200"
                  placeholder="+212600000001"
                  disabled={isLoading}
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-200 mb-2">
                  {editingUser ? 'Nouveau mot de passe (facultatif)' : 'Mot de passe *'}
                </label>
                <input
                  type="password"
                  value={formData.password}
                  onChange={(e) => setFormData({ ...formData, password: e.target.value })}
                  className="w-full px-3 py-2 border border-gray-700 rounded-md focus:outline-none focus:ring-2 focus:ring-afriquia-orange text-white bg-gray-900 hover:bg-gray-800 transition-colors duration-200"
                  placeholder="Mot de passe"
                  disabled={isLoading}
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-200 mb-2">Rôle *</label>
                <select
                  value={formData.role}
                  onChange={(e) => setFormData({ ...formData, role: e.target.value as 'ROLE_LIVREUR' | 'ROLE_CLIENT' })}
                  className="w-full px-3 py-2 border border-gray-700 rounded-md focus:outline-none focus:ring-2 focus:ring-afriquia-orange text-white bg-gray-900 hover:bg-gray-800 transition-colors duration-200"
                  disabled={isLoading}
                >
                  <option value="ROLE_LIVREUR">ROLE_LIVREUR</option>
                  <option value="ROLE_CLIENT">ROLE_CLIENT</option>
                </select>
              </div>
              {formData.role === 'ROLE_LIVREUR' && (
                <div>
                  <label className="block text-sm font-medium text-gray-200 mb-2">Disponible *</label>
                  <select
                    value={formData.available.toString()}
                    onChange={(e) => setFormData({ ...formData, available: e.target.value === 'true' })}
                    className="w-full px-3 py-2 border border-gray-700 rounded-md focus:outline-none focus:ring-2 focus:ring-afriquia-orange text-white bg-gray-900 hover:bg-gray-800 transition-colors duration-200"
                    disabled={isLoading}
                  >
                    <option value="true">Oui</option>
                    <option value="false">Non</option>
                  </select>
                </div>
              )}
            </div>
            <div className="flex gap-2 sm:gap-4 mt-6 sm:mt-8">
              <button
                onClick={editingUser ? handleUpdateUser : handleAddUser}
                className="inline-flex items-center px-3 py-2 sm:px-4 sm:py-2 bg-afriquia-orange text-white rounded-md hover:bg-afriquia-orange/80 transition-colors duration-200 text-sm sm:text-base disabled:opacity-50 shadow-afriquia"
                disabled={isLoading}
              >
                {isLoading ? 'En cours...' : editingUser ? 'Modifier' : 'Ajouter'}
              </button>
              <button
                onClick={() => {
                  setShowForm(false);
                  setEditingUser(null);
                  setFormData({
                    nom: '',
                    email: '',
                    phone: '',
                    password: '',
                    available: true,
                    role: 'ROLE_LIVREUR',
                  });
                }}
                className="inline-flex items-center px-3 py-2 sm:px-4 sm:py-2 bg-gray-800 text-gray-200 rounded-md hover:bg-gray-700 transition-colors duration-200 text-sm sm:text-base disabled:opacity-50 shadow-afriquia"
                disabled={isLoading}
              >
                Annuler
              </button>
            </div>
          </div>
        )}

        <div className="bg-gray-800 rounded-md shadow-afriquia border border-gray-700 overflow-hidden">
          <div className="overflow-x-auto">
            <table className="w-full">
              <thead className="bg-afriquia-blue">
                <tr>
                  <th className="px-4 sm:px-6 py-3 sm:py-4 text-left text-sm font-medium text-white uppercase tracking-wide">Nom</th>
                  <th className="px-4 sm:px-6 py-3 sm:py-4 text-left text-sm font-medium text-white uppercase tracking-wide">Email</th>
                  <th className="px-4 sm:px-6 py-3 sm:py-4 text-left text-sm font-medium text-white uppercase tracking-wide">Téléphone</th>
                  <th className="px-4 sm:px-6 py-3 sm:py-4 text-left text-sm font-medium text-white uppercase tracking-wide">Disponible</th>
                  <th className="px-4 sm:px-6 py-3 sm:py-4 text-left text-sm font-medium text-white uppercase tracking-wide">Rôle</th>
                  <th className="px-4 sm:px-6 py-3 sm:py-4 text-left text-sm font-medium text-white uppercase tracking-wide">Actions</th>
                </tr>
              </thead>
              <tbody className="bg-gray-800 divide-y divide-gray-700">
                {users.map((user) => (
                  <tr key={user.id} className="hover:bg-gray-700 transition-colors duration-200">
                    <td className="px-4 sm:px-6 py-3 sm:py-4 whitespace-nowrap text-sm sm:text-base font-medium text-white">{user.nom}</td>
                    <td className="px-4 sm:px-6 py-3 sm:py-4 whitespace-nowrap text-sm sm:text-base text-gray-200">{user.email}</td>
                    <td className="px-4 sm:px-6 py-3 sm:py-4 whitespace-nowrap text-sm sm:text-base text-gray-200">{user.phone}</td>
                    <td className="px-4 sm:px-6 py-3 sm:py-4 whitespace-nowrap text-sm sm:text-base text-gray-200">
                      {user.role === 'ROLE_LIVREUR' ? (user.available ? 'Oui' : 'Non') : '-'}
                    </td>
                    <td className="px-4 sm:px-6 py-3 sm:py-4 whitespace-nowrap text-sm sm:text-base text-gray-200">{user.role}</td>
                    <td className="px-4 sm:px-6 py-3 sm:py-4 whitespace-nowrap text-sm font-medium">
                      <div className="flex gap-2">
                        <button
                          onClick={() => handleEditUser(user)}
                          className="text-gray-200 hover:text-white p-2 rounded-md hover:bg-afriquia-orange/20 transition-colors duration-200"
                          disabled={isLoading}
                        >
                          <Edit className="h-4 w-4 sm:h-5 sm:w-5" strokeWidth={1.5} />
                        </button>
                        <button
                          onClick={() => handleDeleteUser(user.id, user.role)}
                          className="text-afriquia-red hover:text-afriquia-red/80 p-2 rounded-md hover:bg-afriquia-red/20 transition-colors duration-200"
                          disabled={isLoading}
                        >
                          <Trash2 className="h-4 w-4 sm:h-5 sm:w-5" strokeWidth={1.5} />
                        </button>
                        {user.role === 'ROLE_LIVREUR' && (
                          <button
                            onClick={() => handleToggleAvailability(user.id)}
                            className="text-green-500 hover:text-green-400 p-2 rounded-md hover:bg-green-500/20 transition-colors duration-200"
                            disabled={isLoading}
                          >
                            <User className="h-4 w-4 sm:h-5 sm:w-5" strokeWidth={1.5} />
                          </button>
                        )}
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
          {users.length === 0 && (
            <div className="text-center py-12">
              <p className="text-sm sm:text-base text-gray-200">Aucun utilisateur trouvé</p>
            </div>
          )}
        </div>
        <div className="flex justify-between mt-6">
          <button
            onClick={() => setCurrentPage((prev) => Math.max(prev - 1, 0))}
            disabled={currentPage === 0 || isLoading}
            className="inline-flex items-center px-3 py-2 sm:px-4 sm:py-2 bg-gray-800 text-gray-200 rounded-md hover:bg-gray-700 transition-colors duration-200 text-sm sm:text-base disabled:opacity-50 shadow-afriquia"
          >
            Précédent
          </button>
          <span className="text-sm sm:text-base text-gray-200">Page {currentPage + 1} sur {totalPages}</span>
          <button
            onClick={() => setCurrentPage((prev) => prev + 1)}
            disabled={currentPage >= totalPages - 1 || isLoading}
            className="inline-flex items-center px-3 py-2 sm:px-4 sm:py-2 bg-gray-800 text-gray-200 rounded-md hover:bg-gray-700 transition-colors duration-200 text-sm sm:text-base disabled:opacity-50 shadow-afriquia"
          >
            Suivant
          </button>
        </div>
      </div>
    </div>
  );
};

export default UserManagement;