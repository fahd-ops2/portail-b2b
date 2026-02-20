
import { useState } from 'react';
import { ShoppingCart, X, Plus, Minus, Trash2 } from 'lucide-react';
import { useCart } from '../contexts/CartContext';
import { API_BASE_URL } from '@/config/api';

interface CartProps {
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

const Cart = ({ showToast }: CartProps) => {
  const [isOpen, setIsOpen] = useState(false);
  const { items, removeFromCart, updateQuantity, clearCart, getTotalItems, getTotalPrice } = useCart();

  const orderProduct= async (newOrder: any) => {
    try {
      // Step 1: Get auth token
      const token = localStorage.getItem('authToken');
      if (!token) {
        showToast('Veuillez vous connecter pour renouveler une commande', 'error');
        return;
      }

  
      // Step 6: Create the new order
      const createResponse = await fetch(`${API_BASE_URL}/order`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`
        },
        body: newOrder ? JSON.stringify(newOrder) : '{}',
      });
  
      // Step 7: Handle result of creation
      if (!createResponse.ok) {
        const errorData = await createResponse.json();
        const message = errorData.status === 403 ? 'Accès refusé' : errorData.message;
        throw new Error(message);
      }
    
    } catch (error: any) {
      showToast(error.message || 'Erreur serveur', 'error');
    }
  };

  const handleCheckout = () => {
    if (items.length === 0) {
      showToast('Votre panier est vide', 'error');
      return;
    }

    const client_id : number = 2;

    const order_items = items.map((item: any) => ({
      product_id: item.id,
      quantity: item.quantity,
      prix_unitaire: item.prix_unitaire
    }));

    const newOrder = {
      client_id : client_id,
      delivery_address: '',
      scheduled_for: new Date().toISOString(),
      note: '',
      total_amount: getTotalPrice(),
      status: 'EN_ATTENTE',
      items: order_items.map((item : any) => ({
        product_id: String(item.product_id),
        quantity: item.quantity,
        note: item.note,
        unitPrice: item.prix_unitaire,
        subtotal: getTotalPrice(),
      })),
    };


    orderProduct(newOrder);
    
    showToast(`Commande passée avec succès ! Total: ${getTotalPrice().toFixed(2)}€`, 'success');
    clearCart();
    setIsOpen(false);
  };

  return (
    <>
      {/* Cart Button */}
      <button
        onClick={() => setIsOpen(true)}
        className="fixed bottom-6 right-6 bg-primary text-white p-4 rounded-full shadow-lg hover:bg-blue-700 transition-all duration-200 z-40"
      >
        <ShoppingCart size={24} />
        {getTotalItems() > 0 && (
          <span className="absolute -top-2 -right-2 bg-red-500 text-white text-xs rounded-full w-6 h-6 flex items-center justify-center">
            {getTotalItems()}
          </span>
        )}
      </button>

      {/* Cart Modal */}
      {isOpen && (
        <div className="fixed inset-0 bg-black bg-opacity-50 z-50 flex items-center justify-center p-4">
          <div className="bg-white rounded-lg max-w-2xl w-full max-h-[90vh] overflow-hidden">
            {/* Header */}
            <div className="flex items-center justify-between p-6 border-b border-gray-200">
              <h2 className="text-2xl font-bold text-gray-900">Mon Panier</h2>
              <button
                onClick={() => setIsOpen(false)}
                className="text-gray-500 hover:text-gray-700 transition-colors"
              >
                <X size={24} />
              </button>
            </div>

            {/* Cart Items */}
            <div className="flex-1 overflow-y-auto p-6">
              {items.length === 0 ? (
                <div className="text-center py-12">
                  <ShoppingCart size={48} className="mx-auto text-gray-400 mb-4" />
                  <p className="text-gray-500 text-lg">Votre panier est vide</p>
                </div>
              ) : (
                <div className="space-y-4">
                  {items.map((item) => (
                    <div key={item.id} className="flex items-center space-x-4 p-4 bg-gray-50 rounded-lg">
                      <img
                        src={item.image}
                        alt={item.nom}
                        className="w-16 h-16 object-cover rounded"
                      />
                      <div className="flex-1">
                        <h3 className="font-semibold text-gray-900">{item.nom}</h3>
                        <p className="text-sm text-gray-600">{item.type} - {item.volume}</p>
                        <p className="text-primary font-bold">{item.prix}</p>
                      </div>
                      <div className="flex items-center space-x-2">
                        <button
                          onClick={() => updateQuantity(item.id, item.quantity - 1)}
                          className="p-1 hover:bg-gray-200 rounded"
                        >
                          <Minus size={16} />
                        </button>
                        <span className="w-8 text-center font-semibold">{item.quantity}</span>
                        <button
                          onClick={() => updateQuantity(item.id, item.quantity + 1)}
                          className="p-1 hover:bg-gray-200 rounded"
                        >
                          <Plus size={16} />
                        </button>
                        <button
                          onClick={() => removeFromCart(item.id)}
                          className="p-1 hover:bg-red-100 text-red-600 rounded ml-2"
                        >
                          <Trash2 size={16} />
                        </button>
                      </div>
                    </div>
                  ))}
                </div>
              )}
            </div>

            {/* Footer */}
            {items.length > 0 && (
              <div className="border-t border-gray-200 p-6">
                <div className="flex items-center justify-between mb-4">
                  <span className="text-lg font-semibold">Total:</span>
                  <span className="text-2xl font-bold text-primary">
                    {getTotalPrice().toFixed(2)}€
                  </span>
                </div>
                <div className="flex space-x-4">
                  <button
                    onClick={clearCart}
                    className="flex-1 bg-gray-200 text-gray-800 py-3 px-4 rounded-md hover:bg-gray-300 transition-colors"
                  >
                    Vider le panier
                  </button>
                  <button
                    onClick={handleCheckout}
                    className="flex-1 bg-primary text-white py-3 px-4 rounded-md hover:bg-blue-700 transition-colors"
                  >
                    Passer la commande
                  </button>
                </div>
              </div>
            )}
          </div>
        </div>
      )}
    </>
  );
};

export default Cart;
