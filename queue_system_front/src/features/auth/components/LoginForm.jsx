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
      <div className="mt-4 rounded-lg border border-gray-700 bg-gray-800/50 px-4 py-3 space-y-2">
        <p className="text-center text-xs text-gray-400">
          강의 생성을 위한 학번, 비밀번호는{' '}
          <span className="font-semibold text-white bg-gray-700 px-1.5 py-0.5 rounded">admin</span>{' '}
          입니다.
        </p>
        <div className="border-t border-gray-700" />
        <p className="text-center text-xs text-yellow-400 leading-relaxed">
          ⚠️ 무료 서버를 사용하여 처음 요청 시 최대 1분 정도 대기 시간이 발생할 수 있습니다.
          <br />
          1분이 지나도 응답이 없으면 창을 닫고 재 접속 해주세요.
        </p>
      </div>
    </form>
  );
};
