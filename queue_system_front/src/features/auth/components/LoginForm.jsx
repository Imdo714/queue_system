import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { Input } from '../../../components/shared/Input';
import { Button } from '../../../components/shared/Button';
import { useAuth } from '../hooks/useAuth';

export const LoginForm = () => {
  const [studentId, setStudentId] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();
  const { login } = useAuth();

  const handleSubmit = async (e) => {
    e.preventDefault();
    const success = await login({ studentId, password });
    if (success) {
      navigate('/');
    }
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      <Input
        label="학번"
        type="text"
        placeholder="학번을 입력하세요 (예: 20240001)"
        value={studentId}
        onChange={(e) => setStudentId(e.target.value)}
        required
      />
      <Input
        label="비밀번호"
        type="password"
        placeholder="••••••••"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
        required
      />
      <Button type="submit" className="w-full">
        로그인
      </Button>
      <p className="text-center text-sm text-gray-400">
        계정이 없으신가요?{' '}
        <Link to="/signup" className="text-blue-500 hover:underline">
          회원가입
        </Link>
      </p>
    </form>
  );
};
