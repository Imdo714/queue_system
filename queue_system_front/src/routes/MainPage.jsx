import { useNavigate } from 'react-router-dom';
import { CourseList } from '../features/courses/components/CourseList';
import { CourseRegistration } from '../features/courses/components/CourseRegistration';
import { Button } from '../components/shared/Button';
import { PageHeader } from '../components/shared/PageHeader';
import { useAuth } from '../features/auth/hooks/useAuth';

const MainPage = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <div className="min-h-screen bg-gray-900 text-white p-8">
      <PageHeader
        title="수강신청 시스템"
        subtitle={`환영합니다, ${user?.name || '학생'}님`}
        actions={
          <>
            <Button variant="outline" onClick={() => navigate('/my-courses')}>
              신청 현황
            </Button>
            <Button variant="danger" onClick={handleLogout}>
              로그아웃
            </Button>
          </>
        }
      />

      <main className="max-w-6xl mx-auto">
        <div className="flex justify-between items-center mb-6">
          <h2 className="text-2xl font-semibold">강의 목록</h2>
          <CourseRegistration />
        </div>
        <div className="bg-gray-800 rounded-xl shadow-xl border border-gray-700 overflow-hidden">
          <CourseList />
        </div>
      </main>
    </div>
  );
};

export default MainPage;
