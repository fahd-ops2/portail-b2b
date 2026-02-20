// src/services/productService.ts
import { API_BASE_URL } from '@/config/api';

const getAuthHeaders = () => {
  const token = localStorage.getItem('authToken');
  return {
    'Content-Type': 'application/json',
    ...(token ? { 'Authorization': `Bearer ${token}` } : {}),
  };
};

export const fetchProducts = async (page = 0, size = 10) => {
  const response = await fetch(`${API_BASE_URL}/produits?size=${size}&page=${page}`, {
    method: 'GET',
    headers: getAuthHeaders(),
  });
  if (!response.ok) throw new Error(`Erreur HTTP: ${response.status} ${response.statusText}`);
  return await response.json();
};
