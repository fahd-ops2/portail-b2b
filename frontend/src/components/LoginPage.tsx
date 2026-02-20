import { API_BASE_URL } from '@/config/api';
import { useState } from 'react';

interface LoginPageProps {
  onLogin: () => void;
  onNavigate: (route: string) => void;
  showToast: (message: string, type: 'success' | 'error' | 'info') => void;
}

const LoginPage = ({ onLogin, onNavigate, showToast }: LoginPageProps) => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [isLoading, setIsLoading] = useState(false);

  const handleLogin = async (email: string, password: string) => {
    if (!email || !password) {
      showToast('Veuillez remplir tous les champs', 'error');
      return;
    }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
      showToast('Adresse e-mail invalide', 'error');
      return;
    }

    try {
      const response = await fetch(`${API_BASE_URL}/auth/authenticate`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ email, password }),
      });

      if (!response.ok) {
        const errorData = await response.json();
        showToast(errorData.message || 'Échec de la connexion', 'error');
        return;
      }

      const data = await response.json();
      const token = data.token;

      localStorage.setItem('authToken', token);
      localStorage.setItem('roles', data.roles);

      setIsLoading(true);
      showToast('Connexion réussie', 'success');
      onLogin();
      setIsLoading(false);
      onNavigate('dashboard');
    } catch (error) {
      console.error('Erreur de connexion:', error);
      showToast('Une erreur est survenue', 'error');
    }
  };

  return (
    <div className="min-h-screen bg-login-bg dark:bg-login-bg-dark bg-cover bg-center bg-no-repeat flex items-center justify-center py-12 px-4 sm:px-6 lg:px-8 relative overflow-hidden">
      {/* Overlay avec gradient ajusté pour s'intégrer à l'image */}
      <div className="absolute inset-0 bg-gradient-to-br from-primary/20 via-background/60 to-secondary/20 dark:from-primary/30 dark:via-background/70 dark:to-secondary/30"></div>
      <div className="max-w-md w-full space-y-8 relative z-10">
        <div className="text-center">
          <h2 className="text-4xl font-bold text-white bg-primary/70 px-6 py-3 rounded-lg shadow-md font-afriquia">Connexion</h2>
          <p className="text-muted-foreground/80 text-lg mt-2 font-afriquia">Portail B2B Afriquia Gaz</p>
        </div>

        <div className="bg-card/90 backdrop-blur-sm py-10 px-8 shadow-xl rounded-xl border border-border/50">
          <form onSubmit={(e) => { e.preventDefault(); handleLogin(email, password); }} className="space-y-6">
            <div>
              <label htmlFor="email" className="block text-sm font-medium text-foreground/90 mb-2 font-afriquia">
                Adresse e-mail
              </label>
              <input
                id="email"
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                className="w-full px-4 py-3 border border-input rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-secondary focus:border-transparent transition-all duration-300 bg-card/50 text-foreground placeholder:text-foreground/50"
                placeholder="votre@email.com"
                aria-label="Adresse e-mail"
                required
              />
            </div>

            <div>
              <label htmlFor="password" className="block text-sm font-medium text-foreground/90 mb-2 font-afriquia">
                Mot de passe
              </label>
              <input
                id="password"
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                className="w-full px-4 py-3 border border-input rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-secondary focus:border-transparent transition-all duration-300 bg-card/50 text-foreground placeholder:text-foreground/50"
                placeholder="Votre mot de passe"
                aria-label="Mot de passe"
                required
              />
            </div>

            <button
              type="submit"
              disabled={isLoading}
              className="w-full bg-secondary text-secondary-foreground py-3 px-6 rounded-lg hover:bg-secondary/90 focus:outline-none focus:ring-2 focus:ring-secondary focus:ring-offset-2 transition-all duration-300 disabled:opacity-70 disabled:cursor-not-allowed font-afriquia"
              aria-busy={isLoading}
            >
              {isLoading ? 'Connexion...' : 'Se connecter'}
            </button>

            <div className="text-center">
              <a
                href="#"
                onClick={() => onNavigate('forgot-password')}
                className="text-sm text-secondary hover:text-secondary/80 font-afriquia underline"
              >
                Mot de passe oublié ?
              </a>
            </div>
          </form>
        </div>

        <div className="text-center text-sm text-muted-foreground/80 font-afriquia">
          <p>Compte de test : fahd@gmail.com / password</p>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;