import { Truck, Package, TrendingUp, Clock } from 'lucide-react';

const Dashboard = () => {
  const recentOrders = [
    { id: 'CMD-001', date: '2025-06-05', status: 'Livrée' },
    { id: 'CMD-002', date: '2025-06-04', status: 'En transit' },
    { id: 'CMD-003', date: '2025-06-03', status: 'Validée' },
    { id: 'CMD-004', date: '2025-06-02', status: 'En préparation' },
    { id: 'CMD-005', date: '2025-06-01', status: 'Livrée' },
  ];

  const deliveryUpdates = [
    { order: 'CMD-002', message: 'Commande en route vers votre entrepôt', time: '14:30' },
    { order: 'CMD-001', message: 'Livraison effectuée avec succès', time: '09:15' },
    { order: 'CMD-003', message: 'Commande validée et en préparation', time: 'Hier' },
  ];

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'Livrée': return 'text-green-600 bg-green-100';
      case 'En transit': return 'text-blue-600 bg-blue-100';
      case 'En préparation': return 'text-yellow-600 bg-yellow-100';
      case 'Validée': return 'text-purple-600 bg-purple-100';
      default: return 'text-gray-600 bg-gray-100';
    }
  };

  return (
    <div className="p-6 bg-gray-50 min-h-screen">
      <div className="max-w-7xl mx-auto">
        <h1 className="text-3xl font-bold text-gray-900 mb-8">Tableau de Bord</h1>
        
        {/* Stats Cards */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
          <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-200 hover:shadow-md transition-shadow duration-200">
            <div className="flex items-center">
              <div className="p-3 bg-blue-100 rounded-lg">
                <Package className="h-6 w-6 text-primary" />
              </div>
              <div className="ml-4">
                <p className="text-sm font-medium text-gray-600">Commandes ce mois</p>
                <p className="text-2xl font-bold text-gray-900">24</p>
              </div>
            </div>
          </div>

          <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-200 hover:shadow-md transition-shadow duration-200">
            <div className="flex items-center">
              <div className="p-3 bg-green-100 rounded-lg">
                <Truck className="h-6 w-6 text-green-600" />
              </div>
              <div className="ml-4">
                <p className="text-sm font-medium text-gray-600">Livraisons en cours</p>
                <p className="text-2xl font-bold text-gray-900">3</p>
              </div>
            </div>
          </div>

          <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-200 hover:shadow-md transition-shadow duration-200">
            <div className="flex items-center">
              <div className="p-3 bg-purple-100 rounded-lg">
                <TrendingUp className="h-6 w-6 text-purple-600" />
              </div>
              <div className="ml-4">
                <p className="text-sm font-medium text-gray-600">Consommation totale</p>
                <p className="text-2xl font-bold text-gray-900">2,450L</p>
              </div>
            </div>
          </div>

          <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-200 hover:shadow-md transition-shadow duration-200">
            <div className="flex items-center">
              <div className="p-3 bg-yellow-100 rounded-lg">
                <Clock className="h-6 w-6 text-yellow-600" />
              </div>
              <div className="ml-4">
                <p className="text-sm font-medium text-gray-600">Commandes en attente</p>
                <p className="text-2xl font-bold text-gray-900">2</p>
              </div>
            </div>
          </div>
        </div>

        {/* Main Content Grid */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
          {/* Recent Orders */}
          <div className="bg-white rounded-lg shadow-sm border border-gray-200">
            <div className="p-6 border-b border-gray-200">
              <h2 className="text-xl font-semibold text-gray-900">Commandes Récentes</h2>
            </div>
            <div className="p-6">
              <div className="space-y-4">
                {recentOrders.map((order) => (
                  <div key={order.id} className="flex items-center justify-between p-3 bg-gray-50 rounded-lg hover:bg-gray-100 transition-colors duration-200">
                    <div>
                      <p className="font-medium text-gray-900">{order.id}</p>
                      <p className="text-sm text-gray-600">{order.date}</p>
                    </div>
                    <span className={`px-3 py-1 rounded-full text-xs font-medium ${getStatusColor(order.status)}`}>
                      {order.status}
                    </span>
                  </div>
                ))}
              </div>
              <button className="mt-4 text-primary hover:text-blue-700 text-sm font-medium">
                Voir toutes les commandes →
              </button>
            </div>
          </div>

          {/* Delivery Updates */}
          <div className="bg-white rounded-lg shadow-sm border border-gray-200">
            <div className="p-6 border-b border-gray-200">
              <h2 className="text-xl font-semibold text-gray-900">Mises à Jour des Livraisons</h2>
            </div>
            <div className="p-6">
              <div className="space-y-4">
                {deliveryUpdates.map((update, index) => (
                  <div key={index} className="flex items-start space-x-3 p-3 bg-blue-50 rounded-lg">
                    <div className="p-2 bg-blue-100 rounded-full">
                      <Truck className="h-4 w-4 text-primary" />
                    </div>
                    <div className="flex-1">
                      <p className="text-sm font-medium text-gray-900">{update.order}</p>
                      <p className="text-sm text-gray-600">{update.message}</p>
                      <p className="text-xs text-gray-500 mt-1">{update.time}</p>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          </div>

          {/* Consumption Chart */}
          <div className="bg-white rounded-lg shadow-sm border border-gray-200">
            <div className="p-6 border-b border-gray-200">
              <h2 className="text-xl font-semibold text-gray-900">Consommation de Gaz</h2>
            </div>
            <div className="p-6">
              <div className="space-y-4">
                <div className="flex items-center justify-between">
                  <span className="text-sm font-medium text-gray-700">Propane</span>
                  <span className="text-sm text-gray-600">1,650L (67%)</span>
                </div>
                <div className="w-full bg-gray-200 rounded-full h-2">
                  <div className="bg-primary h-2 rounded-full" style={{ width: '67%' }}></div>
                </div>
                
                <div className="flex items-center justify-between">
                  <span className="text-sm font-medium text-gray-700">Butane</span>
                  <span className="text-sm text-gray-600">800L (33%)</span>
                </div>
                <div className="w-full bg-gray-200 rounded-full h-2">
                  <div className="bg-blue-400 h-2 rounded-full" style={{ width: '33%' }}></div>
                </div>
              </div>
            </div>
          </div>

          {/* Reorder Suggestions */}
          <div className="bg-white rounded-lg shadow-sm border border-gray-200">
            <div className="p-6 border-b border-gray-200">
              <h2 className="text-xl font-semibold text-gray-900">Suggestions de Réapprovisionnement</h2>
            </div>
            <div className="p-6">
              <div className="bg-yellow-50 border border-yellow-200 rounded-lg p-4">
                <p className="text-sm text-gray-700 mb-3">
                  Vous commandez souvent <strong>100L de propane</strong> chaque mois.
                </p>
                <button className="bg-primary text-white px-4 py-2 rounded-md text-sm font-medium hover:bg-blue-700 transition-colors duration-200">
                  Recomander maintenant
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
