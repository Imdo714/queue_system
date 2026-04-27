import { Card } from '../components/shared/Card';
import { SignupForm } from '../features/auth/components/SignupForm';

const SignupPage = () => {
  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-900 px-4">
      <Card title="회원가입" className="w-full max-w-md">
        <SignupForm />
      </Card>
    </div>
  );
};

export default SignupPage;
