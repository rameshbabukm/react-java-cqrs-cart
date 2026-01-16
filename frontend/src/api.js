import axios from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:3001/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

export const customerAPI = {
  getAll: () => api.get('/customers'),
  getById: (id) => api.get(`/customers/${id}`),
  create: (data) => api.post('/customers', data),
  update: (id, data) => api.put(`/customers/${id}`, data),
};

export const categoryAPI = {
  getAll: () => api.get('/categories'),
  getById: (id) => api.get(`/categories/${id}`),
  create: (data) => api.post('/categories', data),
};

export const productAPI = {
  getAll: () => api.get('/products'),
  getByCategoryId: (categoryId) => api.get(`/products/category/${categoryId}`),
  getById: (id) => api.get(`/products/${id}`),
  create: (data) => api.post('/products', data),
};

export const cartAPI = {
  getByCustomerId: (customerId) => api.get(`/carts/customer/${customerId}`),
  addItem: (customerId, data) => api.post(`/carts/${customerId}/items`, data),
  removeItem: (customerId, productId) => api.delete(`/carts/${customerId}/items/${productId}`),
  updateItem: (customerId, productId, data) => api.put(`/carts/${customerId}/items/${productId}`, data),
  checkout: (customerId) => api.post(`/carts/${customerId}/checkout`),
};

export const orderAPI = {
  getByCustomerId: (customerId) => api.get(`/orders/customer/${customerId}`),
  getById: (id) => api.get(`/orders/${id}`),
};

export default api;
