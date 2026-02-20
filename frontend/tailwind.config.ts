import type { Config } from "tailwindcss";

export default {
  darkMode: ['class'],
  content: [
    './pages/**/*.{ts,tsx}',
    './components/**/*.{ts,tsx}',
    './app/**/*.{ts,tsx}',
    './src/**/*.{ts,tsx}',
  ],
  prefix: '',
  theme: {
    container: {
      center: true,
      padding: '2rem',
      screens: {
        '2xl': '1400px',
        'xl': '1280px',
        'lg': '1024px',
        'md': '768px',
        'sm': '640px',
      },
    },
    extend: {
      colors: {
        border: 'hsl(var(--border))',
        input: 'hsl(var(--input))',
        ring: 'hsl(var(--ring))',
        background: 'hsl(var(--background))',
        foreground: 'hsl(var(--foreground))',
        primary: {
          DEFAULT: 'hsl(var(--primary, #004B93))', // Afriquia Blue comme couleur principale
          foreground: 'hsl(var(--primary-foreground, #FFFFFF))', // Blanc pour contraste
        },
        secondary: {
          DEFAULT: 'hsl(var(--secondary, #F97316))', // Orange vif pour accents (inspiré des boutons)
          foreground: 'hsl(var(--secondary-foreground, #FFFFFF))', // Blanc pour contraste
        },
        destructive: {
          DEFAULT: 'hsl(var(--destructive, #D70C1C))', // Afriquia Red pour erreurs
          foreground: 'hsl(var(--destructive-foreground, #FFFFFF))',
        },
        muted: {
          DEFAULT: 'hsl(var(--muted, #F5F5F5))', // Fond clair
          foreground: 'hsl(var(--muted-foreground, #6B7280))', // Gris doux
        },
        accent: {
          DEFAULT: 'hsl(var(--accent, #004B93))', // Réutilise le bleu principal
          foreground: 'hsl(var(--accent-foreground, #FFFFFF))',
        },
        popover: {
          DEFAULT: 'hsl(var(--popover, #FFFFFF))',
          foreground: 'hsl(var(--popover-foreground, #374151))',
        },
        card: {
          DEFAULT: 'hsl(var(--card, #FFFFFF))',
          foreground: 'hsl(var(--card-foreground, #111827))',
        },
        sidebar: {
          DEFAULT: 'hsl(var(--sidebar-background, #F9FAFB))',
          foreground: 'hsl(var(--sidebar-foreground, #1F2937))',
          primary: 'hsl(var(--sidebar-primary, #004B93))',
          'primary-foreground': 'hsl(var(--sidebar-primary-foreground, #FFFFFF))',
          accent: 'hsl(var(--sidebar-accent, #F97316))', // Orange pour accents
          'accent-foreground': 'hsl(var(--sidebar-accent-foreground, #FFFFFF))',
          border: 'hsl(var(--sidebar-border, #E5E7EB))',
          ring: 'hsl(var(--sidebar-ring, #F97316))', // Orange pour focus
        },
        // Couleurs personnalisées pour Afriquia Gaz
        'afriquia-red': '#D70C1C',
        'afriquia-blue': '#004B93',
        'afriquia-light-red': '#FDE7E9',
        'afriquia-orange': '#F97316', // Ajout de l'orange observé
      },
      fontFamily: {
        afriquia: ['Montserrat', 'sans-serif'], // Police principale
      },
      borderRadius: {
        lg: 'var(--radius, 0.5rem)',
        md: 'calc(var(--radius, 0.5rem) - 2px)',
        sm: 'calc(var(--radius, 0.5rem) - 4px)',
      },
      keyframes: {
        'accordion-down': {
          from: { height: '0' },
          to: { height: 'var(--radix-accordion-content-height)' },
        },
        'accordion-up': {
          from: { height: 'var(--radix-accordion-content-height)' },
          to: { height: '0' },
        },
      },
      animation: {
        'accordion-down': 'accordion-down 0.2s ease-out',
        'accordion-up': 'accordion-up 0.2s ease-out',
      },
      backgroundImage: {
        'login-bg': "url('/infra GAZ.png')",
        'login-bg-dark': "url('/infra GAZ-dark.png')",
      },
      spacing: {
        '7': '1.75rem',
        '14': '3.5rem', // Ajout pour la navbar et autres hauteurs
      },
      boxShadow: {
        'afriquia': '0 2px 4px rgba(215, 12, 28, 0.1)', // Ombre subtile avec afriquia-red
        'afriquia-lg': '0 4px 6px rgba(247, 115, 22, 0.1)', // Ombre avec afriquia-orange
      },
      height: {
        '14': '3.5rem', // Hauteur standard pour la navbar
      },
    },
  },
  plugins: [require('tailwindcss-animate')],
} satisfies Config;