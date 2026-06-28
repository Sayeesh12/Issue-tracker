import api from './axios';

export async function login(credentials) {
  const { data } = await api.post('/auth/login', credentials);
  return data;
}

export async function register(userData) {
  const { data } = await api.post('/auth/register', userData);
  return data;
}
