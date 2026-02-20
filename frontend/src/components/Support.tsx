
import { useState } from 'react';
import { MessageSquare, Clock, Check, ChevronDown, ChevronUp } from 'lucide-react';

interface SupportProps {
  showToast: (message: string, type: 'success' | 'error' | 'info') => void;
}

interface Ticket {
  id: number;
  sujet: string;
  description: string;
  urgence: 'Basse' | 'Moyenne' | 'Haute';
  statut: 'Ouvert' | 'Résolu';
  date: string;
  reponse?: string;
}

const Support = ({ showToast }: SupportProps) => {
  const [tickets, setTickets] = useState<Ticket[]>([
    {
      id: 1,
      sujet: 'Problème de connexion',
      description: 'Je n\'arrive pas à me connecter à mon compte',
      urgence: 'Haute',
      statut: 'Résolu',
      date: '2025-06-01',
      reponse: 'Support : Votre problème de connexion a été résolu. Nous avons réinitialisé votre mot de passe.'
    },
    {
      id: 2,
      sujet: 'Question sur la facturation',
      description: 'J\'aimerais comprendre le détail de ma dernière facture',
      urgence: 'Moyenne',
      statut: 'Ouvert',
      date: '2025-06-03'
    }
  ]);

  const [formData, setFormData] = useState({
    sujet: '',
    description: '',
    urgence: 'Moyenne' as 'Basse' | 'Moyenne' | 'Haute'
  });

  const [expandedFAQ, setExpandedFAQ] = useState<number | null>(null);

  const faqItems = [
    {
      id: 1,
      question: 'Comment suivre ma livraison ?',
      answer: 'Vous pouvez suivre votre livraison en temps réel dans la section "Livraisons" de votre tableau de bord. Chaque commande dispose d\'un suivi détaillé avec les étapes de traitement.'
    },
    {
      id: 2,
      question: 'Comment modifier une commande ?',
      answer: 'Les commandes peuvent être modifiées uniquement si elles n\'ont pas encore été expédiées. Contactez notre support pour toute modification.'
    },
    {
      id: 3,
      question: 'Quels sont les délais de livraison ?',
      answer: 'Les délais de livraison varient selon votre localisation : 24-48h en région parisienne, 48-72h en province. Les livraisons sont effectuées du lundi au vendredi.'
    },
    {
      id: 4,
      question: 'Comment signaler un problème de qualité ?',
      answer: 'Utilisez la section "Réclamations" pour signaler tout problème de qualité. Joignez des photos si possible pour accélérer le traitement.'
    },
    {
      id: 5,
      question: 'Comment obtenir une facture ?',
      answer: 'Les factures sont automatiquement générées et envoyées par email après chaque livraison. Vous pouvez également les télécharger depuis votre espace client.'
    }
  ];

  const handleSubmitTicket = () => {
    if (!formData.sujet || !formData.description) {
      showToast('Veuillez remplir tous les champs', 'error');
      return;
    }

    const newTicket: Ticket = {
      id: Math.max(...tickets.map(t => t.id)) + 1,
      sujet: formData.sujet,
      description: formData.description,
      urgence: formData.urgence,
      statut: 'Ouvert',
      date: new Date().toISOString().split('T')[0]
    };

    setTickets([newTicket, ...tickets]);
    setFormData({ sujet: '', description: '', urgence: 'Moyenne' });
    showToast('Ticket de support envoyé avec succès', 'success');
  };

  const getUrgenceColor = (urgence: string) => {
    switch (urgence) {
      case 'Haute':
        return 'bg-red-100 text-red-800';
      case 'Moyenne':
        return 'bg-orange-100 text-orange-800';
      case 'Basse':
        return 'bg-green-100 text-green-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  };

  const getStatutIcon = (statut: string) => {
    return statut === 'Résolu' ? 
      <Check className="text-green-500" size={16} /> : 
      <Clock className="text-orange-500" size={16} />;
  };

  const getStatutColor = (statut: string) => {
    return statut === 'Résolu' ? 
      'bg-green-100 text-green-800' : 
      'bg-orange-100 text-orange-800';
  };

  return (
    <div className="p-6 bg-gray-50 min-h-screen">
      <div className="max-w-7xl mx-auto">
        <h1 className="text-3xl font-bold text-gray-900 mb-8">Support Client</h1>

        <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
          {/* Ticket Form */}
          <div className="bg-white rounded-lg shadow-sm border border-gray-200 p-6">
            <div className="flex items-center gap-3 mb-6">
              <MessageSquare className="text-primary" size={24} />
              <h2 className="text-xl font-semibold text-gray-900">Créer un ticket</h2>
            </div>

            <div className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Sujet *
                </label>
                <input
                  type="text"
                  value={formData.sujet}
                  onChange={(e) => setFormData({ ...formData, sujet: e.target.value })}
                  className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary"
                  placeholder="Résumé du problème"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Description *
                </label>
                <textarea
                  value={formData.description}
                  onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                  rows={4}
                  className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary"
                  placeholder="Décrivez votre problème en détail..."
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Urgence
                </label>
                <select
                  value={formData.urgence}
                  onChange={(e) => setFormData({ ...formData, urgence: e.target.value as 'Basse' | 'Moyenne' | 'Haute' })}
                  className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary"
                >
                  <option value="Basse">Basse</option>
                  <option value="Moyenne">Moyenne</option>
                  <option value="Haute">Haute</option>
                </select>
              </div>

              <button
                onClick={handleSubmitTicket}
                className="w-full bg-primary text-white py-2 px-4 rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-primary focus:ring-offset-2 transition-colors duration-200"
              >
                Envoyer un ticket
              </button>
            </div>
          </div>

          {/* Tickets List */}
          <div className="bg-white rounded-lg shadow-sm border border-gray-200 p-6">
            <h2 className="text-xl font-semibold text-gray-900 mb-6">Mes tickets</h2>

            <div className="space-y-4">
              {tickets.map((ticket) => (
                <div key={ticket.id} className="border border-gray-200 rounded-lg p-4">
                  <div className="flex justify-between items-start mb-3">
                    <div>
                      <h3 className="font-medium text-gray-900">#{ticket.id} - {ticket.sujet}</h3>
                      <p className="text-sm text-gray-600 mt-1">{ticket.description}</p>
                    </div>
                    <div className="flex flex-col items-end gap-2">
                      <div className="flex items-center gap-2">
                        {getStatutIcon(ticket.statut)}
                        <span className={`px-2 py-1 rounded-full text-xs font-medium ${getStatutColor(ticket.statut)}`}>
                          {ticket.statut}
                        </span>
                      </div>
                      <span className={`px-2 py-1 rounded-full text-xs font-medium ${getUrgenceColor(ticket.urgence)}`}>
                        {ticket.urgence}
                      </span>
                    </div>
                  </div>
                  
                  {ticket.reponse && (
                    <div className="bg-blue-50 border border-blue-200 rounded-md p-3 mt-3">
                      <p className="text-sm text-blue-800">{ticket.reponse}</p>
                    </div>
                  )}
                  
                  <p className="text-sm text-gray-500 mt-3">
                    Créé le {new Date(ticket.date).toLocaleDateString('fr-FR')}
                  </p>
                </div>
              ))}

              {tickets.length === 0 && (
                <div className="text-center py-8">
                  <MessageSquare className="mx-auto text-gray-400 mb-4" size={48} />
                  <p className="text-gray-600">Aucun ticket de support</p>
                </div>
              )}
            </div>
          </div>
        </div>

        {/* FAQ Section */}
        <div className="mt-8 bg-white rounded-lg shadow-sm border border-gray-200 p-6">
          <h2 className="text-xl font-semibold text-gray-900 mb-6">Questions Fréquentes</h2>
          
          <div className="space-y-4">
            {faqItems.map((item) => (
              <div key={item.id} className="border border-gray-200 rounded-lg">
                <button
                  onClick={() => setExpandedFAQ(expandedFAQ === item.id ? null : item.id)}
                  className="w-full flex justify-between items-center p-4 text-left hover:bg-gray-50 transition-colors duration-200"
                >
                  <span className="font-medium text-gray-900">{item.question}</span>
                  {expandedFAQ === item.id ? 
                    <ChevronUp className="text-gray-500" size={20} /> : 
                    <ChevronDown className="text-gray-500" size={20} />
                  }
                </button>
                {expandedFAQ === item.id && (
                  <div className="px-4 pb-4 text-gray-700">
                    {item.answer}
                  </div>
                )}
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};

export default Support;
