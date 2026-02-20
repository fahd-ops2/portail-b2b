
import { useState, useEffect } from 'react';
import { User, Edit, Trash2 } from 'lucide-react';
import { fetchClients, addClient, updateClient, deleteClient } from '@/service/clientService';

interface ClientManagementProps {
  showToast: (message: string, type: 'success' | 'error' | 'info') => void;
}

interface Client {
  id: number;
  nom: string;
  email: string;
  address: string;
  role: string;
}

const ClientManagement = ({ showToast }: ClientManagementProps) => {
  const [clients, setClients] = useState<Client[]>([]);
  const [showForm, setShowForm] = useState(false);
  const [editingClient, setEditingClient] = useState<Client | null>(null);
  const [formData, setFormData] = useState({
    nom: '',
    email: '',
    address: '',
    password: '',
    role: 'CLIENT',
  });
  const [isLoading, setIsLoading] = useState(false);
  useEffect(() => {
    const loadClients = async () => {
      setIsLoading(true);
      try {
        const data = await fetchClients();
        setClients(data.content || []);
      } catch (error) {
        showToast(`Erreur lors de la récupération des clients: ${error.message}`, 'error');
      } finally {
        setIsLoading(false);
      }
    };
    loadClients();
  }, []);

  const handleAddClient = async () => {
    if (!formData.nom || !formData.email || !formData.address || !formData.password) {
      return showToast('Veuillez remplir tous les champs', 'error');
    }
    try {
      setIsLoading(true);
      const newClient = await addClient(formData);
      setClients([...clients, newClient]);
      setShowForm(false);
      showToast('Client ajouté avec succès', 'success');
    } catch (error) {
      showToast(`Erreur lors de l'ajout: ${error.message}`, 'error');
    } finally {
      setIsLoading(false);
    }
  };

  const handleUpdateClient = async () => {
    if (!editingClient) return;
    try {
      setIsLoading(true);
      const updated = await updateClient(editingClient.id, formData);
      setClients(clients.map(c => (c.id === editingClient.id ? updated : c)));
      setEditingClient(null);
      setShowForm(false);
      showToast('Client mis à jour avec succès', 'success');
    } catch (error) {
      showToast(`Erreur lors de la mise à jour: ${error.message}`, 'error');
    } finally {
      setIsLoading(false);
    }
  };

  const handleDeleteClient = async (id) => {
    try {
      setIsLoading(true);
      await deleteClient(id);
      setClients(clients.filter(c => c.id !== id));
      showToast('Client supprimé avec succès', 'success');
    } catch (error) {
      showToast(`Erreur lors de la suppression: ${error.message}`, 'error');
    } finally {
      setIsLoading(false);
    }
  };


  return (
    <div className="p-6 bg-gray-50 min-h-screen">
      <div className="max-w-7xl mx-auto">
        <div className="flex justify-between items-center mb-8">
          <h1 className="text-3xl font-bold text-gray-900">Gestion des Clients</h1>
          <button
            onClick={() => {
              setShowForm(!showForm);
              setEditingClient(null);
              setFormData({
                nom: '',
                email: '',
                address: '',
                password: '',
                role: 'CLIENT',
              });
            }}
            className="bg-primary text-white px-6 py-2 rounded-lg hover:bg-blue-700 transition-colors duration-200 flex items-center gap-2"
            disabled={isLoading}
          >
            <User size={20} />
            Ajouter un client
          </button>
        </div>

        {showForm && (
          <div className="bg-white rounded-lg shadow-sm border border-gray-200 p-6 mb-8">
            <h2 className="text-xl font-semibold text-gray-900 mb-4">
              {editingClient ? 'Modifier le client' : 'Ajouter un client'}
            </h2>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div className="md:col-span-2">
                <label className="block text-sm font-medium text-gray-700 mb-2">Nom *</label>
                <input
                  type="text"
                  value={formData.nom}
                  onChange={(e) => setFormData({ ...formData, nom: e.target.value })}
                  className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary"
                  placeholder="Nom du client"
                  disabled={isLoading}
                />
              </div>
              <div className="md:col-span-2">
                <label className="block text-sm font-medium text-gray-700 mb-2">Adresse *</label>
                <textarea
                  value={formData.address}
                  onChange={(e) => setFormData({ ...formData, address: e.target.value })}
                  rows={3}
                  className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary"
                  placeholder="Adresse complète"
                  disabled={isLoading}
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">Email *</label>
                <input
                  type="email"
                  value={formData.email}
                  onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                  className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary"
                  placeholder="contact@entreprise.com"
                  disabled={isLoading}
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  {editingClient ? 'Nouveau mot de passe (facultatif)' : 'Mot de passe *'}
                </label>
                <input
                  type="password"
                  value={formData.password}
                  onChange={(e) => setFormData({ ...formData, password: e.target.value })}
                  className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary"
                  placeholder="Mot de passe"
                  disabled={isLoading}
                />
              </div>
            </div>
            <div className="flex gap-4 mt-6">
              <button
                onClick={editingClient ? handleUpdateClient : handleAddClient}
                className="bg-primary text-white px-6 py-2 rounded-md hover:bg-blue-700 transition-colors duration-200 disabled:opacity-50 disabled:cursor-not-allowed"
                disabled={isLoading}
              >
                {isLoading ? 'En cours...' : editingClient ? 'Modifier' : 'Ajouter'}
              </button>
              <button
                onClick={() => {
                  setShowForm(false);
                  setEditingClient(null);
                  setFormData({
                    nom: '',
                    email: '',
                    address: '',
                    password: '',
                    role: 'CLIENT',
                  });
                }}
                className="bg-gray-500 text-white px-6 py-2 rounded-md hover:bg-gray-600 transition-colors duration-200 disabled:opacity-50 disabled:cursor-not-allowed"
                disabled={isLoading}
              >
                Annuler
              </button>
            </div>
          </div>
        )}

        <div className="bg-white rounded-lg shadow-sm border border-gray-200 overflow-hidden">
          <div className="overflow-x-auto">
            <table className="w-full">
              <thead className="bg-gray-50">
                <tr>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Nom</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Email</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Adresse</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Rôle</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
                {clients.map((client) => (
                  <tr key={client.id} className="hover:bg-gray-50">
                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{client.nom}</td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{client.email}</td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{client.address}</td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{client.role}</td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                      <div className="flex gap-2">
                        <button
                          onClick={() => handleUpdateClient()}
                          className="text-primary hover:text-blue-700 p-1 rounded hover:bg-blue-50 transition-colors duration-200"
                          disabled={isLoading}
                        >
                          <Edit size={16} />
                        </button>
                        <button
                          onClick={() => handleDeleteClient(client.id)}
                          className="text-red-600 hover:text-red-800 p-1 rounded hover:bg-red-50 transition-colors duration-200"
                          disabled={isLoading}
                        >
                          <Trash2 size={16} />
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
          {clients.length === 0 && (
            <div className="text-center py-12">
              <p className="text-gray-500 text-lg">Aucun client trouvé</p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default ClientManagement;
