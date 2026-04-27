import { Card } from '../components/shared/Card';
import { LoginForm } from '../features/auth/components/LoginForm';

const LoginPage = () => {
  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-900 px-4">
      <Card title="로그인" className="w-full max-w-md">
        <LoginForm />
      </Card>
    </div>
  );
};

export default LoginPage;
