
import { useEffect } from 'react';
import { Check, X, AlertCircle } from 'lucide-react';

interface ToastProps {
  message: string;
  type: 'success' | 'error' | 'info';
  onClose: () => void;
  duration?: number;
}

const Toast = ({ message, type, onClose, duration = 3000 }: ToastProps) => {
  useEffect(() => {
    const timer = setTimeout(() => {
      onClose();
    }, duration);

    return () => clearTimeout(timer);
  }, [onClose, duration]);

  const icons = {
    success: <Check size={20} />,
    error: <X size={20} />,
    info: <AlertCircle size={20} />
  };

  const colors = {
    success: 'bg-green-500',
    error: 'bg-red-500',
    info: 'bg-blue-500'
  };

  return (
    <div className={`fixed top-4 right-4 z-50 ${colors[type]} text-white px-6 py-3 rounded-lg shadow-lg flex items-center space-x-2 animate-fade-in`}>
      {icons[type]}
      <span className="font-medium">{message}</span>
      <button onClick={onClose} className="ml-2 hover:bg-black hover:bg-opacity-20 rounded p-1">
        <X size={16} />
      </button>
    </div>
  );
};

export default Toast;
