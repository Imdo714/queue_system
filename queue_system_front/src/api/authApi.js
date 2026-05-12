import api from './axios';

export const authApi = {
  login: (credentials) => api.post('/user/login', credentials),
  signup: (userData) => api.post('/user/register', userData),
};
