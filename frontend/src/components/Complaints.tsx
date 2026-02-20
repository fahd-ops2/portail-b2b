import { useState, useEffect } from 'react';
import { Search, AlertCircle, Upload } from 'lucide-react';
import { fetchComplaints, submitComplaint } from '@/service/complaintsService';

interface ComplaintsProps {
  showToast: (message: string, type: 'success' | 'error' | 'info') => void;
}

interface Complaint {
  id: number;
  orderId: string;
  description: string;
  type: string;
  status: 'EN_ATTENTE' | 'RESOLUE';
  date: string;
  filePath?: string;
}

const Complaints = ({ showToast }: ComplaintsProps) => {
  const [searchTerm, setSearchTerm] = useState('');
  const [filterStatus, setFilterStatus] = useState('all');
  const [filterType, setFilterType] = useState('all');
  const [complaints, setComplaints] = useState<Complaint[]>([]);
  const [formData, setFormData] = useState({
    clientId: 2,
    commandeId: '',
    description: '',
    type: 'GENERAL',
    file: null as File | null,
  });
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const loadComplaints = async () => {
      setLoading(true);
      try {
        const data = await fetchComplaints(page);
        const filtered = data.content.filter((complaint: Complaint) => {
          const matchesSearch =
            (complaint.orderId?.toLowerCase().includes(searchTerm.toLowerCase()) || '') ||
            complaint.description.toLowerCase().includes(searchTerm.toLowerCase());
          const matchesStatus = filterStatus === 'all' || complaint.status === filterStatus;
          const matchesType = filterType === 'all' || complaint.type === filterType;
          return matchesSearch && matchesStatus && matchesType;
        });
        setComplaints(filtered);
        setTotalPages(data.totalPages);
      } catch (err: any) {
        showToast(err.message, 'error');
      } finally {
        setLoading(false);
      }
    };
    loadComplaints();
  }, [page, searchTerm, filterStatus, filterType]);

  const handleSubmitComplaint = async () => {
    if (!formData.commandeId || !formData.description || !formData.type) {
      showToast('Veuillez remplir tous les champs obligatoires', 'error');
      return;
    }
    setLoading(true);
    try {
      await submitComplaint(formData);
      showToast('Réclamation soumise avec succès', 'success');
      const data = await fetchComplaints(page);
      setComplaints(data.content);
      setTotalPages(data.totalPages);
      setFormData({ commandeId: '', description: '', type: 'GENERAL', file: null, clientId: 2 });
    } catch (err: any) {
      showToast(err.message, 'error');
    } finally {
      setLoading(false);
    }
  };

  const getStatusColor = (status: string) => {
    return status === 'RESOLUE'
      ? 'bg-green-100 text-green-800'
      : 'bg-[#F97316] text-white'; // Orange secondaire (#F97316) pour "En attente"
  };

  return (
    <div className="p-6 bg-gray-100 min-h-screen font-montserrat"> {/* Fond clair, pas de noir pur */}
      <div className="max-w-7xl mx-auto">
        <h1 className="text-4xl font-bold text-[#004B93] mb-8 tracking-tight">Retours et Réclamations</h1> {/* Bleu primaire */}

        <div className="bg-white rounded-xl shadow-md border border-gray-200 p-6 mb-8 sticky top-20 z-40">
          <div className="flex flex-col md:flex-row gap-4 items-center">
            <div className="flex-1 relative">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-500" size={20} />
              <input
                type="text"
                placeholder="Rechercher par ID de commande ou description..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="w-full pl-12 pr-4 py-3 border border-gray-300 rounded-xl focus:outline-none focus:ring-2 focus:ring-[#004B93] focus:border-transparent transition-all duration-300 bg-white text-gray-900"
              />
            </div>
            <div className="flex gap-4">
              <select
                value={filterStatus}
                onChange={(e) => setFilterStatus(e.target.value)}
                className="px-4 py-3 border border-gray-300 rounded-xl focus:outline-none focus:ring-2 focus:ring-[#004B93] focus:border-transparent transition-all duration-300 bg-white text-gray-900"
              >
                <option value="all">Tous les statuts</option>
                <option value="EN_ATTENTE">En attente</option>
                <option value="RESOLUE">Résolue</option>
              </select>
              <select
                value={filterType}
                onChange={(e) => setFilterType(e.target.value)}
                className="px-4 py-3 border border-gray-300 rounded-xl focus:outline-none focus:ring-2 focus:ring-[#004B93] focus:border-transparent transition-all duration-300 bg-white text-gray-900"
              >
                <option value="all">Tous les types</option>
                <option value="GENERAL">Général</option>
                <option value="LIVRAISON">Problème de livraison</option>
                <option value="QUALITE">Problème de qualité</option>
              </select>
            </div>
          </div>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
          <div className="bg-white rounded-xl shadow-md border border-gray-200 p-6 sticky top-20 z-40">
            <div className="flex items-center gap-3 mb-6">
              <AlertCircle className="text-[#D70C1C]" size={24} /> {/* Rouge pour alerte */}
              <h2 className="text-xl font-semibold text-[#004B93]">Signaler un problème</h2> {/* Bleu primaire */}
            </div>

            <div className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  ID de commande *
                </label>
                <input
                  type="text"
                  value={formData.commandeId}
                  onChange={(e) => setFormData({ ...formData, commandeId: e.target.value })}
                  className="w-full px-4 py-2 border border-gray-300 rounded-xl focus:outline-none focus:ring-2 focus:ring-[#004B93] font-montserrat text-gray-900"
                  placeholder="CMD-001"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Type de réclamation *
                </label>
                <select
                  value={formData.type}
                  onChange={(e) => setFormData({ ...formData, type: e.target.value })}
                  className="w-full px-4 py-2 border border-gray-300 rounded-xl focus:outline-none focus:ring-2 focus:ring-[#004B93] font-montserrat text-gray-900"
                >
                  <option value="GENERAL">Général</option>
                  <option value="LIVRAISON">Problème de livraison</option>
                  <option value="QUALITE">Problème de qualité</option>
                </select>
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Description du problème *
                </label>
                <textarea
                  value={formData.description}
                  onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                  rows={4}
                  className="w-full px-4 py-2 border border-gray-300 rounded-xl focus:outline-none focus:ring-2 focus:ring-[#004B93] font-montserrat text-gray-900"
                  placeholder="Décrivez le problème rencontré..."
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Preuve (optionnel)
                </label>
                <div className="border-2 border-dashed border-gray-300 rounded-xl p-6 text-center hover:border-[#F97316] transition-colors duration-200">
                  <Upload className="mx-auto text-gray-500 mb-2" size={32} />
                  <p className="text-sm text-gray-600">
                    Cliquez pour télécharger une image ou un document
                  </p>
                  <input
                    type="file"
                    accept="image/*,.pdf,.doc,.docx"
                    onChange={(e) => setFormData({ ...formData, file: e.target.files?.[0] || null })}
                    className="hidden"
                  />
                </div>
                {formData.file && (
                  <p className="text-sm text-gray-600 mt-2">
                    Fichier sélectionné: {formData.file.name}
                  </p>
                )}
              </div>

              <button
                onClick={handleSubmitComplaint}
                disabled={loading}
                className={`w-full py-3 px-6 rounded-xl transition-all duration-300 ${
                  loading
                    ? 'bg-gray-400 text-gray-600 cursor-not-allowed'
                    : 'bg-[#F97316] text-white hover:bg-[#F97316]/90 focus:outline-none focus:ring-2 focus:ring-[#004B93]'
                }`}
              >
                {loading ? 'Envoi en cours...' : 'Soumettre une réclamation'}
              </button>
            </div>
          </div>

          <div className="grid grid-cols-1 gap-6">
            {loading && (
              <div className="text-center py-12">
                <p className="text-gray-500 text-lg font-montserrat">Chargement des réclamations...</p>
              </div>
            )}

            {!loading && complaints.length === 0 && (
              <div className="text-center py-12">
                <p className="text-gray-500 text-lg font-montserrat">Aucune réclamation trouvée pour ces critères</p>
              </div>
            )}

            {!loading &&
              complaints.map((complaint) => (
                <div
                  key={complaint.id}
                  className="bg-white rounded-xl shadow-md border border-gray-200 p-6 hover:shadow-lg hover:scale-[1.02] transition-all duration-300"
                >
                  <div className="flex items-center justify-between mb-3">
                    <h3 className="text-xl font-semibold text-[#004B93]">
                      Réclamation #{complaint.id}
                    </h3>
                    <span
                      className={`text-xs px-2 py-1 rounded-full ${getStatusColor(
                        complaint.status
                      )}`}
                    >
                      {complaint.status === 'EN_ATTENTE' ? 'En attente' : 'Résolue'}
                    </span>
                  </div>
                  <div className="flex items-center justify-between mb-2">
                    <span className="text-sm text-gray-600">
                      Commande {complaint.orderId || 'Non spécifié'}
                    </span>
                    <span className="text-sm text-gray-600">{complaint.type}</span>
                  </div>
                  <p className="text-gray-700 text-sm mb-3">{complaint.description}</p>
                  <p className="text-xs text-gray-500 mb-3">
                    Soumis le {new Date(complaint.date).toLocaleDateString('fr-FR', { year: 'numeric', month: 'long', day: 'numeric' })}
                  </p>
                  {complaint.filePath && (
                    <p className="text-sm text-[#F97316]">
                      <a href={complaint.filePath} target="_blank" rel="noopener noreferrer" className="underline hover:text-[#F97316]/80">
                        Télécharger le fichier joint
                      </a>
                    </p>
                  )}
                </div>
              ))}

            {totalPages > 1 && (
              <div className="flex justify-between mt-6">
                <button
                  onClick={() => setPage((prev) => Math.max(prev - 1, 0))}
                  disabled={page === 0 || loading}
                  className="px-5 py-2 bg-gray-200 rounded-xl text-gray-700 hover:bg-[#004B93]/10 disabled:opacity-50 transition-all duration-300 font-montserrat"
                >
                  Précédent
                </button>
                <span className="text-base text-gray-700 font-montserrat">Page {page + 1} sur {totalPages}</span>
                <button
                  onClick={() => setPage((prev) => Math.min(prev + 1, totalPages - 1))}
                  disabled={page === totalPages - 1 || loading}
                  className="px-5 py-2 bg-gray-200 rounded-xl text-gray-700 hover:bg-[#004B93]/10 disabled:opacity-50 transition-all duration-300 font-montserrat"
                >
                  Suivant
                </button>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default Complaints;