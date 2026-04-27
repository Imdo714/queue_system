import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { Input } from '../../../components/shared/Input';
import { Button } from '../../../components/shared/Button';
import { useAuth } from '../hooks/useAuth';

export const SignupForm = () => {
  const [formData, setFormData] = useState({
    name: '',
    studentId: '',
    password: '',
    confirmPassword: '',
  });
  const navigate = useNavigate();
  const { signup } = useAuth();

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (formData.password !== formData.confirmPassword) {
      alert('비밀번호가 일치하지 않습니다.');
      return;
    }
    const success = await signup(formData);
    if (success) {
      alert('회원가입이 완료되었습니다. 로그인해주세요.');
      navigate('/login');
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      <Input
        label="이름"
        name="name"
        placeholder="홍길동"
        value={formData.name}
        onChange={handleChange}
        required
      />
      <Input
        label="학번"
        name="studentId"
        type="text"
        placeholder="학번을 입력하세요 (예: 20240001)"
        value={formData.studentId}
        onChange={handleChange}
        required
      />
      <Input
        label="비밀번호"
        name="password"
        type="password"
        placeholder="••••••••"
        value={formData.password}
        onChange={handleChange}
        required
      />
      <Input
        label="비밀번호 확인"
        name="confirmPassword"
        type="password"
        placeholder="••••••••"
        value={formData.confirmPassword}
        onChange={handleChange}
        required
      />
      <Button type="submit" className="w-full">
        회원가입
      </Button>
      <p className="text-center text-sm text-gray-400">
        이미 계정이 있으신가요?{' '}
        <Link to="/login" className="text-blue-500 hover:underline">
          로그인
        </Link>
      </p>
    </form>
  );
};
