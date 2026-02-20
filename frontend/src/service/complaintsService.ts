import axios from 'axios';

const BASE_URL = 'http://localhost:8090/api/v1/reclamation';

export const submitComplaint = async (formData: {
  commandeId: string;
  description: string;
  type: string;
  clientId: number;
}) => {
  const token = localStorage.getItem('authToken');

  const payload = {
    order_id: formData.commandeId,
    description: formData.description,
    type: formData.type,
    client_id: formData.clientId,
  };

  const response = await axios.post(BASE_URL, JSON.stringify(payload), {
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${token}`,
    },
  });

  return response.data;
};



export const fetchComplaints = async (
  page: number,
  size: number = 10,
  searchTerm?: string,
  filterStatus?: string,
  filterType?: string,
  clientId?: number,
) => {
  const params = new URLSearchParams({
    page: page.toString(),
    size: size.toString(),
    ...(searchTerm && { search: searchTerm }),
    ...(filterStatus && filterStatus !== 'all' && { status: filterStatus }),
    ...(filterType && filterType !== 'all' && { type: filterType }),
    ...(clientId && { clientId: clientId.toString() }),
  });

  const token = localStorage.getItem('authToken');


  const response = await axios.get(`${BASE_URL}/filtered?${params}`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
  return response.data;
};