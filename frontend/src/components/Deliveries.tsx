import { useState, useEffect } from 'react';
import { Truck, Check, Search } from 'lucide-react';
import { API_BASE_URL } from '@/config/api';

interface DeliveriesProps {
  showToast: (message: string, type: 'success' | 'error' | 'info') => void;
}

interface Delivery {
  id: number;
  orderId: string;
  livreurId: number;
  status: string;
  scheduledTime: string;
  deliveryAddress: string;
  notes?: string;
  deliveredAt?: string;
  isUrgent: boolean;
}

// Fonction utilitaire pour convertir un tableau [year, month, day, hour, minute] en chaîne ISO
const arrayToIsoDate = (dateArray: any[]): string | null => {
  if (!Array.isArray(dateArray) || dateArray.length < 3) return null;
  const [year, month, day, hour = 0, minute = 0] = dateArray.map(Number);
  if (!Number.isInteger(year) || !Number.isInteger(month) || !Number.isInteger(day)) return null;
  try {
    return `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')}T${String(hour).padStart(2, '0')}:${String(minute).padStart(2, '0')}:00`;
  } catch (error) {
    console.warn('Erreur lors de la conversion du tableau de date:', dateArray, error);
    return null;
  }
};

const Deliveries = ({ showToast }: DeliveriesProps) => {
  const [deliveries, setDeliveries] = useState<Delivery[]>([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [filterStatus, setFilterStatus] = useState('all');
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(1);
  const [loading, setLoading] = useState(false);

  const mapStatusToFrench = (status: string): string => {
    switch (status) {
      case 'SCHEDULED':
        return 'Planifiée';
      case 'IN_PROGRESS':
        return 'En cours';
      case 'DELIVERED':
        return 'Livrée';
      case 'CANCELLED':
        return 'Annulée';
      case 'FAILED':
        return 'Échouée';
      default:
        return 'Inconnu';
    }
  };

  useEffect(() => {
    const fetchDeliveries = async () => {
      setLoading(true);

      try {
        const token = localStorage.getItem('authToken');
        if (!token) {
          throw new Error('Aucun token d’authentification trouvé. Veuillez vous reconnecter.');
        }

        let url = `${API_BASE_URL}/deliveries`;
        if (searchTerm) {
          url = `${url}/search?searchTerm=${encodeURIComponent(searchTerm)}&size=10&page=${page}`;
        } else if (filterStatus !== 'all') {
          url = `${url}/by-status?status=${filterStatus}&size=10&page=${page}`;
        } else {
          url = `${url}?size=10&page=${page}`;
        }

        const res = await fetch(url, {
          headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json',
          },
        });


        if (!res.ok) {
          if (res.status === 401) {
            throw new Error('Session expirée. Veuillez vous reconnecter.');
          } else if (res.status === 403) {
            throw new Error('Accès non autorisé. Vérifiez vos permissions.');
          } else {
            throw new Error(`Erreur HTTP ${res.status}: ${res.statusText || 'Erreur inconnue'}`);
          }
        }

        const data = await res.json();

        if (!data || !Array.isArray(data.content)) {
          throw new Error('Réponse inattendue: le champ "content" est manquant ou invalide');
        }

        const mappedDeliveries = data.content.map((delivery: any) => {
          // Gestion des champs snake_case comme fallback
          const orderId = delivery.orderId || delivery.order_id || 'Inconnu';
          const livreurId = delivery.livreurId || delivery.livreur_id || 0;
          const status = mapStatusToFrench(delivery.status || 'UNKNOWN');
          const deliveryAddress = delivery.deliveryAddress || delivery.delivery_address || 'Non spécifiée';
          const notes = delivery.notes || undefined;
          const isUrgent = delivery.isUrgent || delivery.is_urgent || false;

          // Conversion de scheduled_time (tableau ou chaîne)
          let scheduledTime = '';
          if (delivery.scheduledTime && typeof delivery.scheduledTime === 'string' && !isNaN(new Date(delivery.scheduledTime).getTime())) {
            scheduledTime = delivery.scheduledTime;
          } else if (delivery.scheduled_time && Array.isArray(delivery.scheduled_time)) {
            const isoDate = arrayToIsoDate(delivery.scheduled_time);
            scheduledTime = isoDate && !isNaN(new Date(isoDate).getTime()) ? isoDate : '';
          }

          // Conversion de deliveredAt (tableau ou chaîne)
          let deliveredAt: string | undefined;
          if (delivery.deliveredAt && typeof delivery.deliveredAt === 'string' && !isNaN(new Date(delivery.deliveredAt).getTime())) {
            deliveredAt = delivery.deliveredAt;
          } else if (delivery.delivered_at && Array.isArray(delivery.delivered_at)) {
            const isoDate = arrayToIsoDate(delivery.delivered_at);
            deliveredAt = isoDate && !isNaN(new Date(isoDate).getTime()) ? isoDate : undefined;
          }

          // Warning uniquement si les champs critiques manquent complètement
          if (!delivery.id || (!delivery.orderId && !delivery.order_id) || !delivery.status) {
            console.warn('Données de livraison critiques manquantes:', delivery);
          }

          return {
            id: delivery.id || 0,
            orderId,
            livreurId,
            status,
            scheduledTime,
            deliveryAddress,
            notes,
            deliveredAt,
            isUrgent,
          };
        });

        setDeliveries(mappedDeliveries);
        setTotalPages(data.totalPages || data.total_pages || 1);
        showToast('Livraisons chargées avec succès', 'success');
      } catch (error: any) {
        console.error('Erreur lors de la récupération des livraisons:', error);
        showToast(error.message || 'Erreur lors du chargement des livraisons', 'error');
      } finally {
        console.log('Fin de fetchDeliveries, loading mis à false');
        setLoading(false);
      }
    };

    fetchDeliveries();
  }, [page, filterStatus, searchTerm]);

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'Planifiée':
        return 'bg-blue-100 text-blue-800';
      case 'En cours':
        return 'bg-orange-100 text-orange-800';
      case 'Livrée':
        return 'bg-green-100 text-green-800';
      case 'Annulée':
        return 'bg-red-100 text-red-800';
      case 'Échouée':
        return 'bg-gray-100 text-gray-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  };

  const TimelineStep = ({
    label,
    time,
    isCompleted,
    isActive,
  }: {
    label: string;
    time?: string;
    isCompleted: boolean;
    isActive: boolean;
  }) => (
    <div className="flex items-center mb-4 last:mb-0">
      <div
        className={`w-4 h-4 rounded-full mr-3 flex items-center justify-center ${
          isCompleted ? 'bg-green-500' : isActive ? 'bg-blue-500' : 'bg-gray-300'
        }`}
      >
        {isCompleted && <Check size={10} className="text-white" />}
      </div>
      <div className="flex-1">
        <div className={`font-medium ${isCompleted || isActive ? 'text-gray-900' : 'text-gray-500'}`}>
          {label}
        </div>
        {time && (
          <div className="text-sm text-gray-500">
            {new Date(time).toLocaleString('fr-FR', { dateStyle: 'medium', timeStyle: 'short' })}
          </div>
        )}
      </div>
    </div>
  );

  const getTimeline = (delivery: Delivery) => ({
    planifiee: delivery.scheduledTime,
    enCours:
      delivery.status === 'En cours' || delivery.status === 'Livrée' || delivery.status === 'Annulée' || delivery.status === 'Échouée'
        ? delivery.scheduledTime
        : undefined,
    livree: delivery.status === 'Livrée' ? delivery.deliveredAt : undefined,
    annulee: delivery.status === 'Annulée' ? delivery.scheduledTime : undefined,
    echouee: delivery.status === 'Échouée' ? delivery.scheduledTime : undefined,
  });

  return (
    <div className="p-6 bg-gray-50 min-h-screen">
      <div className="max-w-7xl mx-auto">
        <h1 className="text-3xl font-bold text-gray-900 mb-8">Suivi des Livraisons</h1>

        <div className="bg-white rounded-lg shadow-sm border border-gray-200 p-6 mb-8 sticky top-20 z-40">
          <div className="flex flex-col md:flex-row gap-4">
            <div className="flex-1 relative">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400" size={20} />
              <input
                type="text"
                id="delivery-search"
                name="deliverySearch"
                placeholder="Rechercher par ID de commande ou adresse..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary"
                autoComplete="off"
                aria-label="Rechercher des livraisons"
              />
            </div>
            <div className="flex gap-4">
              <select
                id="delivery-status-filter"
                name="deliveryStatusFilter"
                value={filterStatus}
                onChange={(e) => setFilterStatus(e.target.value)}
                className="px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary"
                aria-label="Filtrer par statut"
              >
                <option value="all">Tous les statuts</option>
                <option value="SCHEDULED">Planifiée</option>
                <option value="IN_PROGRESS">En cours</option>
                <option value="DELIVERED">Livrée</option>
                <option value="CANCELLED">Annulée</option>
                <option value="FAILED">Échouée</option>
              </select>
            </div>
          </div>
        </div>

        {loading ? (
          <div className="text-center py-12" aria-live="polite">
            Chargement...
          </div>
        ) : (
          <div className="space-y-6">
            {deliveries.map((delivery) => (
              <div
                key={delivery.id}
                className="bg-white rounded-lg shadow-sm border border-gray-200 p-6 hover:shadow-lg transition-all duration-200"
              >
                <div className="flex flex-col lg:flex-row lg:justify-between lg:items-start mb-6">
                  <div className="mb-4 lg:mb-0">
                    <div className="flex items-center gap-3 mb-2">
                      <h2 className="text-xl font-semibold text-gray-900">
                        Commande {delivery.orderId}
                      </h2>
                      <span
                        className={`px-3 py-1 rounded-full text-sm font-medium ${getStatusColor(delivery.status)}`}
                      >
                        {delivery.status}
                      </span>
                    </div>
                    <p className="text-gray-600 mb-1">{delivery.notes || 'Aucun détail spécifié'}</p>
                    <p className="text-gray-500 text-sm">{delivery.deliveryAddress}</p>
                    {delivery.isUrgent && (
                      <p className="text-red-600 text-sm font-medium mt-1">Livraison urgente</p>
                    )}
                  </div>
                  <div className="text-right">
                    <p className="text-sm text-gray-500">Date programmée</p>
                    <p className="font-medium">
                      {delivery.scheduledTime
                        ? new Date(delivery.scheduledTime).toLocaleString('fr-FR', {
                            dateStyle: 'medium',
                            timeStyle: 'short',
                          })
                        : 'Date invalide'}
                    </p>
                    {delivery.deliveredAt && (
                      <>
                        <p className="text-sm text-gray-500 mt-2">Date de livraison</p>
                        <p className="font-medium text-green-600">
                          {new Date(delivery.deliveredAt).toLocaleString('fr-FR', {
                            dateStyle: 'medium',
                            timeStyle: 'short',
                          })}
                        </p>
                      </>
                    )}
                  </div>
                </div>

                <div className="border-t border-gray-200 pt-6">
                  <h3 className="text-lg font-medium text-gray-900 mb-4">Suivi de la livraison</h3>
                  <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
                    <TimelineStep
                      label="Planifiée"
                      time={getTimeline(delivery).planifiee}
                      isCompleted={true}
                      isActive={delivery.status === 'Planifiée'}
                    />
                    <TimelineStep
                      label="En cours"
                      time={getTimeline(delivery).enCours}
                      isCompleted={!!getTimeline(delivery).enCours}
                      isActive={delivery.status === 'En cours'}
                    />
                    <TimelineStep
                      label="Livrée"
                      time={getTimeline(delivery).livree}
                      isCompleted={!!getTimeline(delivery).livree}
                      isActive={delivery.status === 'Livrée'}
                    />
                    <TimelineStep
                      label="Annulée/Échouée"
                      time={getTimeline(delivery).annulee || getTimeline(delivery).echouee}
                      isCompleted={!!getTimeline(delivery).annulee || !!getTimeline(delivery).echouee}
                      isActive={delivery.status === 'Annulée' || delivery.status === 'Échouée'}
                    />
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}

        {deliveries.length === 0 && !loading && (
          <div className="bg-white rounded-lg shadow-sm border border-gray-200 p-8 text-center">
            <Truck className="mx-auto text-gray-400 mb-4" size={48} />
            <p className="text-gray-600">Aucune livraison trouvée pour ces critères</p>
          </div>
        )}

        {totalPages > 0 && (
          <div className="mt-6 flex justify-center gap-4">
            <button
              onClick={() => setPage(page - 1)}
              disabled={page === 0}
              className="px-4 py-2 bg-gray-200 rounded disabled:opacity-50 hover:bg-gray-300 transition-colors duration-200"
              aria-label="Page précédente"
            >
              Précédent
            </button>
            <span className="self-center" aria-live="polite">
              Page {page + 1} sur {totalPages}
            </span>
            <button
              onClick={() => setPage(page + 1)}
              disabled={page >= totalPages - 1}
              className="px-4 py-2 bg-gray-200 rounded disabled:opacity-50 hover:bg-gray-300 transition-colors duration-200"
              aria-label="Page suivante"
            >
              Suivant
            </button>
          </div>
        )}
      </div>
    </div>
  );
};

export default Deliveries;