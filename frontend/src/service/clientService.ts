import { API_BASE_URL } from '@/config/api';

const getAuthHeaders = () => {
  const token = localStorage.getItem('authToken');
  return {
    'Content-Type': 'application/json',
    ...(token ? { 'Authorization': `Bearer ${token}` } : {}),
  };
};

export const fetchClients = async (page = 0, size = 10) => {
  const response = await fetch(`${API_BASE_URL}/clients?size=${size}&page=${page}`, {
    method: 'GET',
    headers: getAuthHeaders(),
  });
  if (!response.ok) throw new Error(`Erreur HTTP: ${response.status} ${response.statusText}`);
  return await response.json();
};

export const addClient = async (clientData: any) => {
  const response = await fetch(`${API_BASE_URL}/clients`, {
    method: 'POST',
    headers: getAuthHeaders(),
    body: JSON.stringify(clientData),
  });
  if (!response.ok) throw new Error(`Erreur HTTP: ${response.status} ${response.statusText}`);
  return await response.json();
};

export const updateClient = async (id: number, clientData: any) => {
  const response = await fetch(`${API_BASE_URL}/clients/${id}`, {
    method: 'PUT',
    headers: getAuthHeaders(),
    body: JSON.stringify(clientData),
  });
  if (!response.ok) throw new Error(`Erreur HTTP: ${response.status} ${response.statusText}`);
  return await response.json();
};

export const deleteClient = async (id: number) => {
  const response = await fetch(`${API_BASE_URL}/clients/${id}`, {
    method: 'DELETE',
    headers: getAuthHeaders(),
  });
  if (!response.ok) throw new Error(`Erreur HTTP: ${response.status} ${response.statusText}`);
  return true;
};
