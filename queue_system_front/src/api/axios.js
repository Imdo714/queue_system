import axios from 'axios';

const api = axios.create({
  baseURL: 'https://queue-system-pym2.onrender.com',
  headers: {
    'Content-Type': 'application/json',
  },
});

// Response interceptor to handle BaseResponse format
api.interceptors.response.use(
  (response) => {
    const { data } = response;
    // backend follows { code: number, status: string, message: string, data: T }
    // Successful response (typically 200 OK)
    if (data.code >= 200 && data.code < 300) {
      return data.data;
    }
    return Promise.reject(data.message || 'Unknown Error');
  },
  (error) => {
    // Standardize error message extraction
    const message = error.response?.data?.message || error.message || 'Network Error';
    return Promise.reject(message);
  }
);

export default api;
