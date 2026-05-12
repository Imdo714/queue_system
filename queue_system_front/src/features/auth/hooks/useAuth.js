import { useAuthStore } from '../../../store/useAuthStore';
import { authApi } from '../../../api/authApi';

/**
 * Custom hook for authentication logic.
 */
export const useAuth = () => {
  const { user, isAuthenticated, login: storeLogin, logout: storeLogout } = useAuthStore();

  const login = async (credentials) => {
    try {
      const response = await authApi.login({
        studentNo: credentials.studentId,
        password: credentials.password
      });
      // response should already be the data inside BaseResponse.data due to interceptor
      storeLogin(response);
      return true;
    } catch (error) {
      console.error('Login failed:', error);
      alert(error || '로그인에 실패했습니다.');
      return false;
    }
  };

  const signup = async (userData) => {
    try {
      await authApi.signup({
        studentNo: userData.studentId,
        password: userData.password,
        name: userData.name
      });
      return true;
    } catch (error) {
      console.error('Signup failed:', error);
      alert(error || '회원가입에 실패했습니다.');
      return false;
    }
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
