import { useState, useEffect } from 'react';
import { User, Edit, Trash2 } from 'lucide-react';
import {
  fetchLivreurs,
  addLivreur,
  updateLivreur,
  deleteLivreur,
  toggleLivreurAvailability,
} from '../service/livreurService';

interface LivreurManagementProps {
  showToast: (message: string, type: 'success' | 'error' | 'info') => void;
}

interface Livreur {
  id: number;
  nom: string;
  email: string;
  phone: string;
  available: boolean;
  role: string;
}

const LivreurManagement = ({ showToast }: LivreurManagementProps) => {
  const [livreurs, setLivreurs] = useState<Livreur[]>([]);
  const [showForm, setShowForm] = useState(false);
  const [editingLivreur, setEditingLivreur] = useState<Livreur | null>(null);
  const [formData, setFormData] = useState({
    nom: '',
    email: '',
    phone: '',
    password: '',
    available: true,
    role: 'LIVREUR',
  });
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    const loadLivreurs = async () => {
      setIsLoading(true);
      try {
        const data = await fetchLivreurs();
        if (!Array.isArray(data)) {
          showToast("La réponse de l'API n'est pas au format attendu", 'error');
          setLivreurs([]);
        } else {
          setLivreurs(data);
        }
      } catch (error: any) {
        showToast(`Erreur lors de la récupération des livreurs: ${error.message}`, 'error');
        setLivreurs([]);
      } finally {
        setIsLoading(false);
      }
    };
    loadLivreurs();
  }, []);

  const validateForm = () => {
    if (!formData.nom || !formData.email || !formData.phone || (!editingLivreur && !formData.password)) {
      showToast('Veuillez remplir tous les champs obligatoires', 'error');
      return false;
    }
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(formData.email)) {
      showToast('Adresse e-mail invalide', 'error');
      return false;
    }
    const phoneRegex = /^\+?\d{10,15}$/;
    if (!phoneRegex.test(formData.phone)) {
      showToast('Numéro de téléphone invalide', 'error');
      return false;
    }
    if (!editingLivreur && formData.password.length < 6) {
      showToast('Le mot de passe doit contenir au moins 6 caractères', 'error');
      return false;
    }
    return true;
  };

  const handleAddLivreur = async () => {
    if (!validateForm()) return;
    setIsLoading(true);
    try {
      const newLivreur = await addLivreur(formData);
      setLivreurs([...livreurs, newLivreur]);
      setFormData({
        nom: '',
        email: '',
        phone: '',
        password: '',
        available: true,
        role: 'LIVREUR',
      });
      setShowForm(false);
      showToast('Livreur ajouté avec succès', 'success');
    } catch (error: any) {
      showToast(`Erreur lors de l'ajout du livreur: ${error.message}`, 'error');
    } finally {
      setIsLoading(false);
    }
  };

  const handleEditLivreur = (livreur: Livreur) => {
    setEditingLivreur(livreur);
    setFormData({
      nom: livreur.nom,
      email: livreur.email,
      phone: livreur.phone,
      password: '',
      available: livreur.available,
      role: livreur.role,
    });
    setShowForm(true);
  };

  const handleUpdateLivreur = async () => {
    if (!editingLivreur) return;
    if (!validateForm()) return;

    setIsLoading(true);
    try {
      const updatedLivreur = await updateLivreur(editingLivreur.id, formData);
      setLivreurs(livreurs.map(l => (l.id === editingLivreur.id ? updatedLivreur : l)));
      setFormData({
        nom: '',
        email: '',
        phone: '',
        password: '',
        available: true,
        role: 'LIVREUR',
      });
      setShowForm(false);
      setEditingLivreur(null);
      showToast('Livreur modifié avec succès', 'success');
    } catch (error: any) {
      showToast(`Erreur lors de la mise à jour du livreur: ${error.message}`, 'error');
    } finally {
      setIsLoading(false);
    }
  };

  const handleDeleteLivreur = async (livreurId: number) => {
    setIsLoading(true);
    try {
      await deleteLivreur(livreurId);
      setLivreurs(livreurs.filter(l => l.id !== livreurId));
      showToast('Livreur supprimé', 'success');
    } catch (error: any) {
      showToast(`Erreur lors de la suppression du livreur: ${error.message}`, 'error');
    } finally {
      setIsLoading(false);
    }
  };

  const handleToggleAvailability = async (livreurId: number) => {
    setIsLoading(true);
    try {
      const updatedLivreur = await toggleLivreurAvailability(livreurId);
      setLivreurs(livreurs.map(l => (l.id === livreurId ? updatedLivreur : l)));
      showToast('Disponibilité du livreur mise à jour', 'success');
    } catch (error: any) {
      showToast(`Erreur lors de la mise à jour de la disponibilité: ${error.message}`, 'error');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="p-6 bg-gray-50 min-h-screen">
      <div className="max-w-7xl mx-auto">
        <div className="flex justify-between items-center mb-8">
          <h1 className="text-3xl font-bold text-gray-900">Gestion des Livreurs</h1>
          <button
            onClick={() => {
              setShowForm(!showForm);
              setEditingLivreur(null);
              setFormData({
                nom: '',
                email: '',
                phone: '',
                password: '',
                available: true,
                role: 'LIVREUR',
              });
            }}
            className="bg-primary text-white px-6 py-2 rounded-lg hover:bg-blue-700 transition-colors duration-200 flex items-center gap-2"
            disabled={isLoading}
          >
            <User size={20} />
            Ajouter un livreur
          </button>
        </div>

        {showForm && (
          <div className="bg-white rounded-lg shadow-sm border border-gray-200 p-6 mb-8">
            <h2 className="text-xl font-semibold text-gray-900 mb-4">
              {editingLivreur ? 'Modifier le livreur' : 'Ajouter un livreur'}
            </h2>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div className="md:col-span-2">
                <label className="block text-sm font-medium text-gray-700 mb-2">Nom *</label>
                <input
                  type="text"
                  value={formData.nom}
                  onChange={(e) => setFormData({ ...formData, nom: e.target.value })}
                  className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary"
                  placeholder="Nom du livreur"
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
                <label className="block text-sm font-medium text-gray-700 mb-2">Téléphone *</label>
                <input
                  type="tel"
                  value={formData.phone}
                  onChange={(e) => setFormData({ ...formData, phone: e.target.value })}
                  className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary"
                  placeholder="+212600000001"
                  disabled={isLoading}
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  {editingLivreur ? 'Nouveau mot de passe (facultatif)' : 'Mot de passe *'}
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
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">Disponible *</label>
                <select
                  value={formData.available.toString()}
                  onChange={(e) => setFormData({ ...formData, available: e.target.value === 'true' })}
                  className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary"
                  disabled={isLoading}
                >
                  <option value="true">Oui</option>
                  <option value="false">Non</option>
                </select>
              </div>
            </div>
            <div className="flex gap-4 mt-6">
              <button
                onClick={editingLivreur ? handleUpdateLivreur : handleAddLivreur}
                className="bg-primary text-white px-6 py-2 rounded-md hover:bg-blue-700 transition-colors duration-200 disabled:opacity-50 disabled:cursor-not-allowed"
                disabled={isLoading}
              >
                {isLoading ? 'En cours...' : editingLivreur ? 'Modifier' : 'Ajouter'}
              </button>
              <button
                onClick={() => {
                  setShowForm(false);
                  setEditingLivreur(null);
                  setFormData({
                    nom: '',
                    email: '',
                    phone: '',
                    password: '',
                    available: true,
                    role: 'LIVREUR',
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
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Téléphone</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Disponible</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Rôle</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
                {livreurs.map((livreur) => (
                  <tr key={livreur.id} className="hover:bg-gray-50">
                    <td className="px-6 py-4 whitespace-nowrap text-sm">{livreur.nom}</td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{livreur.email}</td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{livreur.phone}</td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{livreur.available ? 'Oui' : 'Non'}</td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{livreur.role}</td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                      <div className="flex gap-2">
                        <button
                          onClick={() => handleEditLivreur(livreur)}
                          className="text-primary hover:text-blue-700 p-1 rounded hover:bg-blue-50 transition-colors duration-200"
                          disabled={isLoading}
                        >
                          <Edit size={16} />
                        </button>
                        <button
                          onClick={() => handleDeleteLivreur(livreur.id)}
                          className="text-red-600 hover:text-red-800 p-1 rounded hover:bg-red-50 transition-colors duration-200"
                          disabled={isLoading}
                        >
                          <Trash2 size={16} />
                        </button>
                        <button
                          onClick={() => handleToggleAvailability(livreur.id)}
                          className="text-green-600 hover:text-green-800 p-1 rounded hover:bg-green-50 transition-colors duration-200"
                          disabled={isLoading}
                        >
                          <User size={16} />
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
          {livreurs.length === 0 && (
            <div className="text-center py-12">
              <p className="text-gray-500 text-lg">Aucun livreur trouvé</p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default LivreurManagement;
