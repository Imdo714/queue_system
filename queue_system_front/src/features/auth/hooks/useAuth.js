import { useAuthStore } from '../../../store/useAuthStore';

/**
 * Custom hook for authentication logic.
 * Wraps Zustand store to facilitate future API integration.
 */
export const useAuth = () => {
  const { user, isAuthenticated, login: storeLogin, logout: storeLogout } = useAuthStore();

  const login = async (credentials) => {
    // Future API call: const response = await api.login(credentials);
    console.log('Logging in with API (simulated)...', credentials);
    storeLogin({ studentId: credentials.studentId, name: '사용자' });
    return true;
  };

  const signup = async (userData) => {
    // Future API call: await api.signup(userData);
    console.log('Signing up with API (simulated)...', userData);
    return true;
  };

  const logout = () => {
    storeLogout();
  };

  return {
    user,
    isAuthenticated,
    login,
    signup,
    logout,
  };
};
