import { API_BASE_URL } from '@/config/api';

const getAuthToken = () => localStorage.getItem('authToken');

const getHeaders = (hasBody = false) => {
  const token = getAuthToken();
  return {
    'Content-Type': hasBody ? 'application/json' : 'application/json',
    ...(token ? { Authorization: `Bearer ${token}` } : {}),
  };
};

export const fetchLivreurs = async () => {
  const response = await fetch(`${API_BASE_URL}/livreurs`, {
    method: 'GET',
    headers: getHeaders(),
  });
  if (!response.ok) throw new Error(`Erreur HTTP: ${response.status} ${response.statusText}`);
  return response.json();
};

export const addLivreur = async (livreurData: any) => {
  const response = await fetch(`${API_BASE_URL}/livreurs`, {
    method: 'POST',
    headers: getHeaders(true),
    body: JSON.stringify(livreurData),
  });
  if (!response.ok) throw new Error(`Erreur HTTP: ${response.status} ${response.statusText}`);
  return response.json();
};

export const updateLivreur = async (id: number, livreurData: any) => {
  const response = await fetch(`${API_BASE_URL}/livreurs/${id}`, {
    method: 'PUT',
    headers: getHeaders(true),
    body: JSON.stringify(livreurData),
  });
  if (!response.ok) throw new Error(`Erreur HTTP: ${response.status} ${response.statusText}`);
  return response.json();
};

export const deleteLivreur = async (id: number) => {
  const response = await fetch(`${API_BASE_URL}/livreurs/${id}`, {
    method: 'DELETE',
    headers: getHeaders(),
  });
  if (!response.ok) throw new Error(`Erreur HTTP: ${response.status} ${response.statusText}`);
  return response.text();
};

export const toggleLivreurAvailability = async (id: number) => {
  const response = await fetch(`${API_BASE_URL}/livreurs/${id}/toggle`, {
    method: 'PATCH',
    headers: getHeaders(),
  });
  if (!response.ok) throw new Error(`Erreur HTTP: ${response.status} ${response.statusText}`);
  return response.json();
};
