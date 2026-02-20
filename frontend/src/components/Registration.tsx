import React, { useState } from 'react';
import { User } from 'lucide-react';

interface RegistrationProps {
  onRegister: (data: any) => void;
  showToast: (message: string, type: 'success' | 'error' | 'info') => void;
}

const Registration = ({ onRegister, showToast }: RegistrationProps) => {
  const [formData, setFormData] = useState({
    nomEntreprise: '',
    adresse: '',
    telephone: '',
    contactPrincipal: '',
    emailContact: '',
    siret: ''
  });

  const [isLoading, setIsLoading] = useState(false);

  const handleRegister = async () => {
    // Validation
    if (
      !formData.nomEntreprise ||
      !formData.adresse ||
      !formData.telephone ||
      !formData.contactPrincipal ||
      !formData.emailContact ||
      !formData.siret
    ) {
      showToast('Veuillez remplir tous les champs', 'error');
      return;
    }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(formData.emailContact)) {
      showToast('Adresse e-mail invalide', 'error');
      return;
    }

    if (formData.siret.length !== 14 || !/^\d{14}$/.test(formData.siret)) {
      showToast('Le numéro SIRET doit contenir exactement 14 chiffres', 'error');
      return;
    }

    const phoneRegex = /^\+?\d{10,15}$/;
    if (!phoneRegex.test(formData.telephone)) {
      showToast('Numéro de téléphone invalide', 'error');
      return;
    }

    setIsLoading(true);
    console.log('Tentative d\'inscription avec:', formData);

    // Simulate API call
    setTimeout(() => {
      setIsLoading(false);
      showToast('Inscription réussie ! Votre compte est en cours de validation.', 'success');
      console.log('Inscription simulée réussie, appel de onRegister');
      onRegister(formData);
    }, 1500);
  };

  return (
    <div className="min-h-screen bg-gray-50 flex items-center justify-center py-12 px-4 sm:px-6 lg:px-8">
      <div className="max-w-2xl w-full space-y-8">
        <div className="text-center">
          <User className="mx-auto text-primary" size={48} />
          <h2 className="text-3xl font-bold text-gray-900 mb-2">Inscription</h2>
          <p className="text-gray-600">Créez votre compte entreprise Afriquia Gaz</p>
        </div>

        <div className="bg-white py-8 px-6 shadow-lg rounded-lg border border-gray-200">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div className="md:col-span-2">
              <label htmlFor="nomEntreprise" className="block text-sm font-medium text-gray-700 mb-2">
                Nom de l'entreprise *
              </label>
              <input
                id="nomEntreprise"
                type="text"
                value={formData.nomEntreprise}
                onChange={(e) => setFormData({ ...formData, nomEntreprise: e.target.value })}
                className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary"
                placeholder="Nom de votre entreprise"
              />
            </div>

            <div className="md:col-span-2">
              <label htmlFor="adresse" className="block text-sm font-medium text-gray-700 mb-2">
                Adresse complète *
              </label>
              <textarea
                id="adresse"
                value={formData.adresse}
                onChange={(e) => setFormData({ ...formData, adresse: e.target.value })}
                rows={3}
                className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary"
                placeholder="Adresse complète de l'entreprise"
              />
            </div>

            <div>
              <label htmlFor="telephone" className="block text-sm font-medium text-gray-700 mb-2">
                Numéro de téléphone *
              </label>
              <input
                id="telephone"
                type="tel"
                value={formData.telephone}
                onChange={(e) => setFormData({ ...formData, telephone: e.target.value })}
                className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary"
                placeholder="+212123456789"
              />
            </div>

            <div>
              <label htmlFor="siret" className="block text-sm font-medium text-gray-700 mb-2">
                Numéro SIRET *
              </label>
              <input
                id="siret"
                type="text"
                value={formData.siret}
                onChange={(e) => setFormData({ ...formData, siret: e.target.value.replace(/\D/g, '') })}
                maxLength={14}
                className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary"
                placeholder="12345678901234"
              />
            </div>

            <div>
              <label htmlFor="contactPrincipal" className="block text-sm font-medium text-gray-700 mb-2">
                Contact principal *
              </label>
              <input
                id="contactPrincipal"
                type="text"
                value={formData.contactPrincipal}
                onChange={(e) => setFormData({ ...formData, contactPrincipal: e.target.value })}
                className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary"
                placeholder="Nom du responsable"
              />
            </div>

            <div>
              <label htmlFor="emailContact" className="block text-sm font-medium text-gray-700 mb-2">
                Email du contact *
              </label>
              <input
                id="emailContact"
                type="email"
                value={formData.emailContact}
                onChange={(e) => setFormData({ ...formData, emailContact: e.target.value })}
                className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary"
                placeholder="contact@entreprise.com"
              />
            </div>
          </div>

          <div className="mt-8">
            <button
              onClick={handleRegister}
              disabled={isLoading}
              className="w-full bg-primary text-white py-3 px-4 rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-primary focus:ring-offset-2 transition-colors duration-200 disabled:opacity-50 disabled:cursor-not-allowed"
            >
              {isLoading ? 'Inscription en cours...' : 'S\'inscrire'}
            </button>
          </div>

          <div className="mt-6 text-center text-sm text-gray-600">
            <p>
              En vous inscrivant, vous acceptez nos{' '}
              <a href="#" className="text-primary hover:text-blue-700">
                conditions d'utilisation
              </a>{' '}
              et notre{' '}
              <a href="#" className="text-primary hover:text-blue-700">
                politique de confidentialité
              </a>
            </p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Registration;