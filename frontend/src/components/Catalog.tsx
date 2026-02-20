import { useEffect, useState } from 'react';
import { Search, Filter, ChevronDown, ShoppingCart } from 'lucide-react';
import { useCart } from '../contexts/CartContext';
import Cart from './Cart';
import { fetchProducts } from '@/service/productService';

interface CatalogProps {
  showToast: (message: string, type: 'success' | 'error' | 'info') => void;
}

const Catalog = ({ showToast }: CatalogProps) => {
  const [searchTerm, setSearchTerm] = useState('');
  const [filterType, setFilterType] = useState('all');
  const [filterVolume, setFilterVolume] = useState('all');
  const [sortOption, setSortOption] = useState('default');
  const { addToCart } = useCart();
  const [products, setProducts] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [currentPage, setCurrentPage] = useState(1);
  const productsPerPage = 6;

  useEffect(() => {
    const loadProducts = async () => {
      setIsLoading(true);
      try {
        const data = await fetchProducts();
        let filtered = data.content.filter((product: any) => {
          const matchesSearch =
            product.nom.toLowerCase().includes(searchTerm.toLowerCase()) ||
            product.type.toLowerCase().includes(searchTerm.toLowerCase());
          const matchesType = filterType === 'all' || product.type === filterType;
          const matchesVolume = filterVolume === 'all' || product.volume === filterVolume;
          return matchesSearch && matchesType && matchesVolume;
        });

        // Tri des produits
        switch (sortOption) {
          case 'price_asc':
            filtered.sort((a: any, b: any) => parseFloat(a.prix) - parseFloat(b.prix));
            break;
          case 'price_desc':
            filtered.sort((a: any, b: any) => parseFloat(b.prix) - parseFloat(a.prix));
            break;
          case 'name_asc':
            filtered.sort((a: any, b: any) => a.nom.localeCompare(b.nom));
            break;
          default:
            break;
        }

        setProducts(filtered);
      } catch (error: any) {
        console.error('Erreur lors du chargement des produits:', error);
        showToast(error.message || 'Échec du chargement des produits', 'error');
      } finally {
        setIsLoading(false);
      }
    };

    loadProducts();
  }, [searchTerm, filterType, filterVolume, sortOption]);

  const handleAddToCart = (product: any) => {
    addToCart(product);
    showToast(`${product.nom} ajouté au panier`, 'success');
  };

  // Pagination
  const indexOfLastProduct = currentPage * productsPerPage;
  const indexOfFirstProduct = indexOfLastProduct - productsPerPage;
  const currentProducts = products.slice(indexOfFirstProduct, indexOfLastProduct);
  const totalPages = Math.ceil(products.length / productsPerPage);

  const paginate = (pageNumber: number) => setCurrentPage(pageNumber);

  return (
    <div className="p-6 bg-gray-100 min-h-screen font-sans">
      <div className="max-w-7xl mx-auto">
        <h1 className="text-4xl font-bold text-blue-950 mb-8 tracking-tight">Catalogue des Produits</h1>

        {/* Filters & Sort */}
        <div className="bg-white rounded-xl shadow-lg border border-orange-200/30 p-6 mb-8">
          <div className="flex flex-col md:flex-row gap-6">
            <div className="flex gap-6 flex-wrap">
              <select
                value={filterType}
                onChange={(e) => setFilterType(e.target.value)}
                className="px-5 py-3 border border-blue-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-orange-400 focus:border-transparent transition-all duration-300 bg-gray-50"
              >
                <option value="all">Tous les types</option>
                <option value="Propane">Propane</option>
                <option value="Butane">Butane</option>
              </select>

              <select
                value={filterVolume}
                onChange={(e) => setFilterVolume(e.target.value)}
                className="px-5 py-3 border border-blue-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-orange-400 focus:border-transparent transition-all duration-300 bg-gray-50"
              >
                <option value="all">Tous les volumes</option>
                {['25L', '50L', '75L', '100L', '200L', '500L'].map((volume) => (
                  <option key={volume} value={volume}>
                    {volume}
                  </option>
                ))}
              </select>

              <select
                value={sortOption}
                onChange={(e) => setSortOption(e.target.value)}
                className="px-5 py-3 border border-blue-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-orange-400 focus:border-transparent transition-all duration-300 bg-gray-50 flex items-center"
              >
                <option value="default">Trier par</option>
                <option value="price_asc">Prix : Croissant</option>
                <option value="price_desc">Prix : Décroissant</option>
                <option value="name_asc">Nom : A-Z</option>
              </select>
            </div>
          </div>
        </div>

        {/* Products */}
        {isLoading ? (
          <p className="text-center text-blue-900 text-lg animate-pulse">Chargement des produits...</p>
        ) : (
          <>
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-8 mb-8">
              {currentProducts.map((product: any) => (
                <div
                  key={product.id}
                  className="bg-white rounded-xl shadow-xl border border-orange-100/50 overflow-hidden hover:shadow-2xl hover:-translate-y-2 transition-all duration-300 group"
                >
                  <img src={product.image} alt={product.nom} className="w-full h-64 object-cover" />
                  <div className="p-6">
                    <div className="flex items-center justify-between mb-3">
                      <h3 className="text-2xl font-semibold text-blue-950 line-clamp-1">{product.nom}</h3>
                      <span className="text-xs bg-orange-100 text-orange-700 px-3 py-1 rounded-full uppercase tracking-wide">{product.type}</span>
                    </div>

                    <div className="flex items-center justify-between mb-4">
                      <span className="text-xl font-bold text-orange-600">{product.prix}5O MAD</span>
                      <span className="text-sm text-blue-800/70">{product.volume}</span>
                    </div>

                    <p className="text-gray-600 text-base mb-5 line-clamp-2">{product.description}</p>

                    <button
                      onClick={() => handleAddToCart(product)}
                      className="w-full bg-gradient-to-r from-orange-500 to-orange-600 text-white py-3 px-6 rounded-lg hover:from-orange-600 hover:to-orange-700 focus:outline-none focus:ring-2 focus:ring-orange-400 focus:ring-offset-2 transition-all duration-300 flex items-center justify-center gap-2 group-hover:scale-105"
                    >
                      <ShoppingCart size={18} />
                      Ajouter au panier
                    </button>
                  </div>
                </div>
              ))}
            </div>

            {/* Pagination */}
            <div className="flex justify-center items-center gap-4 mt-8">
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

        {products.length === 0 && !isLoading && (
          <div className="text-center py-16">
            <p className="text-blue-800/70 text-xl">Aucun produit trouvé pour ces critères</p>
          </div>
        )}
      </div>

      {/* Policy Section */}
      <footer className="mt-12 bg-blue-950 text-white p-6 rounded-lg shadow-inner">
        <div className="max-w-7xl mx-auto text-center">
          <h2 className="text-2xl font-semibold mb-4">Politique</h2>
          <p className="text-gray-300 mb-2">Livraison gratuite à partir de 500 MAD. Retours acceptés dans les 14 jours.</p>
          <p className="text-gray-300">Paiement sécurisé par carte ou virement bancaire.</p>
          <p className="text-gray-300 mt-4">© 2025 Allo Gaz. Tous droits réservés.</p>
        </div>
      </footer>

      <Cart showToast={showToast} />
    </div>
  );
};

export default Catalog;