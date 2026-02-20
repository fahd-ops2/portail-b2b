
import { useState, useEffect, useCallback } from 'react';
import { API_BASE_URL } from '@/config/api';
import { fetchProducts } from '@/service/productService';
import { Search, Filter, ChevronDown, ShoppingBag } from 'lucide-react';

interface OrdersProps {
  showToast: (message: string, type: 'success' | 'error' | 'info') => void;
}

interface OrderItem {
  product_id: string;
  quantity: number;
  note: string;
  unitPrice: number;
  subtotal: number;
  label?: string;
  productName?: string;
}

interface Order {
  id: string;
  client_id: number;
  created_at: Date;
  items: OrderItem[];
  deliveryAddress: string;
  note: string;
  total_amount: number;
  status: string;
}

interface Product {
  id: string;
  nom: string;
  prix: number;
  type: string;
  volume: string;
}

const Orders = ({ showToast }: OrdersProps) => {
  const [orders, setOrders] = useState<Order[]>([]);
  const [products, setProducts] = useState<Product[]>([]);
  const [isLoadingOrders, setIsLoadingOrders] = useState(false);
  const [isLoadingProducts, setIsLoadingProducts] = useState(false);
  const [searchTerm, setSearchTerm] = useState('');
  const [filterStatus, setFilterStatus] = useState('all');
  const [currentPage, setCurrentPage] = useState(1);
  const ordersPerPage = 5;

  const convertToDate = (localDateTimeArray) => {
    const [year, month, day, hour, minute, second] = localDateTimeArray;
    return new Date(year, month - 1, day, hour, minute, second);
  };

  const getStatusColor = (status: string) => {
    switch (status.toUpperCase()) {
      case 'LIVREE':
        return 'text-green-700 bg-green-50 border-green-200';
      case 'EN_COURS':
        return 'text-blue-700 bg-blue-50 border-blue-200';
      case 'EN_ATTENTE':
        return 'text-yellow-700 bg-yellow-50 border-yellow-200';
      case 'VALIDEE':
        return 'text-purple-700 bg-purple-50 border-purple-200';
      case 'ANNULEE':
        return 'text-red-700 bg-red-50 border-red-200';
      default:
        return 'text-gray-700 bg-gray-50 border-gray-200';
    }
  };

  const fetchClientAddress = useCallback(async (clientId: number) => {
    try {
      const token = localStorage.getItem('authToken');
      if (!token) {
        showToast('Veuillez vous connecter pour voir les détails du client', 'error');
        return null;
      }

      const response = await fetch(`${API_BASE_URL}/clients/${clientId}`, {
        headers: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
      });

      if (!response.ok) {
        console.error(`Erreur HTTP ${response.status} pour client ${clientId}:`, await response.text());
        throw new Error(`Erreur lors de la récupération de l'adresse du client ${clientId}`);
      }
      const clientData = await response.json();
      console.log(`Client ${clientId} data:`, clientData);
      return clientData.address || `Adresse du client ${clientId} non trouvée`;
    } catch (error: any) {
      console.error('Erreur dans fetchClientAddress:', error);
      showToast(error.message || 'Erreur serveur', 'error');
      return `Erreur pour client ${clientId}`;
    }
  }, [showToast]);

  const fetchOrders = useCallback(async () => {
    setIsLoadingOrders(true);
    try {
      const token = localStorage.getItem('authToken');
      if (!token) {
        showToast('Veuillez vous connecter pour voir l\'historique', 'error');
        return;
      }

      const response = await fetch(`${API_BASE_URL}/order?size=10&page=0`, {
        headers: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
      });

      if (!response.ok) throw new Error('Erreur lors du chargement des commandes');
      const data = await response.json();
      let filteredOrders = data.content || [];

      console.log('Orders from API:', filteredOrders);

      // Récupérer l'adresse du client pour chaque commande
      const ordersWithClientAddress = await Promise.all(filteredOrders.map(async (order) => {
        const clientAddress = await fetchClientAddress(order.client_id);
        console.log(`Order ${order.id} - Client ID: ${order.client_id}, Address: ${clientAddress}`);
        return { ...order, deliveryAddress: clientAddress };
      }));

      // Filtrer par statut et recherche
      if (filterStatus !== 'all') {
        filteredOrders = ordersWithClientAddress.filter((order) => order.status.toUpperCase() === filterStatus.toUpperCase());
      }
      if (searchTerm) {
        filteredOrders = ordersWithClientAddress.filter((order) =>
          order.id.toLowerCase().includes(searchTerm.toLowerCase()) ||
          order.items.some((item) => item.label?.toLowerCase().includes(searchTerm.toLowerCase()) || item.productName?.toLowerCase().includes(searchTerm.toLowerCase()))
        );
      }

      setOrders(filteredOrders);
    } catch (error: any) {
      showToast(error.message || 'Erreur serveur', 'error');
    } finally {
      setIsLoadingOrders(false);
    }
  }, [showToast, filterStatus, searchTerm, fetchClientAddress]);

  useEffect(() => {
    const loadProducts = async () => {
      setIsLoadingProducts(true);
      try {
        const data = await fetchProducts();
        setProducts(data.content || []);
      } catch (error) {
        showToast(`Erreur lors de la récupération des clients: ${error.message}`, 'error');
      } finally {
        setIsLoadingProducts(false);
      }
    };

    loadProducts();
    fetchOrders();
  }, [fetchOrders]);

  const handleReorder = async (orderId: string) => {
    try {
      const token = localStorage.getItem('authToken');
      if (!token) {
        showToast('Veuillez vous connecter pour renouveler une commande', 'error');
        return;
      }

      const orderResponse = await fetch(`${API_BASE_URL}/order/${orderId}`, {
        headers: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
      });

      if (!orderResponse.ok) {
        showToast('Erreur lors de la récupération de la commande', 'error');
        return;
      }

      const order = await orderResponse.json();

      if (!order.items || order.items.length === 0) {
        showToast('Aucun article trouvé dans cette commande', 'error');
        return;
      }

      const clientAddress = await fetchClientAddress(order.client_id);
      const newOrder = {
        client_id: order.client_id,
        delivery_address: clientAddress || '',
        scheduled_for: new Date().toISOString(),
        note: order.note || '',
        total_amount: order.total_amount,
        status: 'EN_ATTENTE',
        items: order.items.map((item: OrderItem) => ({
          product_id: String(item.product_id),
          quantity: item.quantity,
          note: item.note,
          unitPrice: item.unitPrice,
          subtotal: item.subtotal,
        })),
      };

      const createResponse = await fetch(`${API_BASE_URL}/order`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`,
        },
        body: newOrder ? JSON.stringify(newOrder) : '{}',
      });

      if (!createResponse.ok) {
        const errorData = await createResponse.json();
        const message = errorData.status === 403 ? 'Accès refusé' : errorData.message;
        throw new Error(message);
      }

      showToast(`Commande ${orderId} renouvelée avec succès`, 'success');
      fetchOrders();
    } catch (error: any) {
      showToast(error.message || 'Erreur serveur', 'error');
    }
  };

  // Pagination
  const indexOfLastOrder = currentPage * ordersPerPage;
  const indexOfFirstOrder = indexOfLastOrder - ordersPerPage;
  const currentOrders = orders.slice(indexOfFirstOrder, indexOfLastOrder);
  const totalPages = Math.ceil(orders.length / ordersPerPage);

  const paginate = (pageNumber: number) => setCurrentPage(pageNumber);

  return (
    <div className="p-6 bg-gray-100 min-h-screen font-sans">
      <div className="max-w-7xl mx-auto">
        <h1 className="text-4xl font-bold text-blue-950 mb-6 tracking-tight">Historique des Commandes</h1>

        {/* Search and Filters */}
        <div className="bg-white rounded-xl shadow-lg border border-orange-200/30 p-4 mb-6 flex flex-col md:flex-row gap-4 sticky top-16 z-40 backdrop-blur-sm bg-opacity-95">
          <div className="relative flex-1">
            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-blue-800/60" size={16} />
            <input
              type="text"
              placeholder="Rechercher une commande..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="w-full pl-10 pr-4 py-2 border border-blue-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-orange-400 focus:border-transparent transition-all duration-300 text-sm bg-gray-50"
            />
          </div>
          <select
            value={filterStatus}
            onChange={(e) => setFilterStatus(e.target.value)}
            className="px-4 py-2 border border-blue-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-orange-400 focus:border-transparent transition-all duration-300 bg-gray-50"
          >
            <option value="all">Tous les statuts</option>
            <option value="LIVREE">Livrée</option>
            <option value="EN_COURS">En cours</option>
            <option value="EN_ATTENTE">En attente</option>
            <option value="VALIDEE">Validée</option>
            <option value="ANNULEE">Annulée</option>
          </select>
        </div>

        {/* Orders Table */}
        <div className="bg-white rounded-xl shadow-xl border border-orange-100/50 p-6">
          {isLoadingOrders ? (
            <p className="text-center text-blue-900 text-lg animate-pulse">Chargement des commandes...</p>
          ) : orders.length === 0 ? (
            <p className="text-center text-blue-800/70 text-xl">Aucune commande trouvée.</p>
          ) : (
            <>
              <div className="overflow-x-auto rounded-xl">
                <table className="min-w-full bg-white text-base">
                  <thead className="bg-gradient-to-r from-blue-50 to-blue-100 text-blue-900 uppercase text-xs font-semibold tracking-wide">
                    <tr>
                      <th className="px-6 py-3 text-left">N° Commande</th>
                      <th className="px-6 py-3 text-left">Date</th>
                      <th className="px-6 py-3 text-left">Produits</th>
                      <th className="px-6 py-3 text-left">Adresse</th>
                      <th className="px-6 py-3 text-left">Statut</th>
                      <th className="px-6 py-3 text-left">Total</th>
                      <th className="px-6 py-3 text-left">Actions</th>
                    </tr>
                  </thead>
                  <tbody className="divide-y divide-orange-100">
                    {currentOrders.map((order) => (
                      <tr
                        key={order.id}
                        className="hover:bg-orange-50/30 transition-all duration-300 group"
                      >
                        <td className="px-6 py-4 font-medium text-blue-900">
                          {order.id}
                        </td>
                        <td className="px-6 py-4 text-blue-800/80">
                          {convertToDate(order?.created_at).toLocaleString('fr-FR', {
                            year: 'numeric',
                            month: 'long',
                            day: 'numeric',
                            hour: '2-digit',
                            minute: '2-digit',
                          })}
                        </td>
                        <td className="px-6 py-4">
                          <div className="bg-orange-50/20 rounded-lg border border-orange-100 p-3 max-w-md">
                            <div className="space-y-2">
                              {order.items.slice(0, 5).map((item, index) => (
                                <div key={index} className="flex justify-between text-sm">
                                  <span className="text-blue-900">
                                    {item.label || item.productName || `ID: ${item.product_id}`}
                                  </span>
                                  <span className="text-blue-800/80">{item.quantity} unité(s)</span>
                                </div>
                              ))}
                              {order.items.length > 5 && (
                                <p className="text-blue-600/60 text-sm italic">
                                  + {order.items.length - 5} autre(s) produit(s)...
                                </p>
                              )}
                            </div>
                          </div>
                        </td>
                        <td className="px-6 py-4 text-blue-800/80">
                          {order.deliveryAddress || 'Adresse non spécifiée'}
                        </td>
                        <td className="px-6 py-4">
                          <span
                            className={`inline-flex items-center px-3 py-1 rounded-full text-sm font-medium ${getStatusColor(
                              order.status
                            )} border`}
                          >
                            {order.status}
                          </span>
                        </td>
                        <td className="px-6 py-4 font-semibold text-orange-600">
                          {order?.total_amount?.toFixed(2)} MAD
                        </td>
                        <td className="px-6 py-4">
                          <button
                            onClick={() => handleReorder(order.id)}
                            className="flex items-center gap-2 text-base font-medium text-orange-600 hover:text-orange-700 transition-all duration-300 rounded-lg px-4 py-2 hover:bg-orange-100"
                            aria-label={`Renouveler la commande ${order.id}`}
                          >
                            <ShoppingBag size={18} />
                            Commander à nouveau
                          </button>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>

              {/* Pagination */}
              <div className="flex justify-center items-center gap-4 mt-6">
                <button
                  onClick={() => paginate(currentPage - 1)}
                  disabled={currentPage === 1}
                  className="px-4 py-2 bg-blue-900 text-white rounded-lg hover:bg-blue-800 disabled:bg-blue-400 transition-colors duration-300"
                >
                  Précédent
                </button>
                {Array.from({ length: totalPages }, (_, i) => i + 1).map((number) => (
                  <button
                    key={number}
                    onClick={() => paginate(number)}
                    className={`px-4 py-2 rounded-lg ${currentPage === number ? 'bg-orange-500 text-white' : 'bg-white text-blue-900 border border-blue-200 hover:bg-blue-50'} transition-colors duration-300`}
                  >
                    {number}
                  </button>
                ))}
                <button
                  onClick={() => paginate(currentPage + 1)}
                  disabled={currentPage === totalPages}
                  className="px-4 py-2 bg-blue-900 text-white rounded-lg hover:bg-blue-800 disabled:bg-blue-400 transition-colors duration-300"
                >
                  Suivant
                </button>
              </div>
            </>
          )}
        </div>
      </div>
    </div>
  );
};

export default Orders;
