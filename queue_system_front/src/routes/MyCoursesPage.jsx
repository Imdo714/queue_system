import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Button } from '../components/shared/Button';
import { useCourses } from '../features/courses/hooks/useCourses';
import { useAuth } from '../features/auth/hooks/useAuth';
import { MyRegistrationsTable } from '../features/courses/components/MyRegistrationsTable';

const MyCoursesPage = () => {
  const { myRegistrations, getMyRegistrations, cancel, isLoading, error } = useCourses();
  const { user } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    getMyRegistrations();
  }, []); // eslint-disable-line react-hooks/exhaustive-deps

  const handleCancel = async (courseId) => {
    if (!window.confirm('정말로 수강 신청을 취소하시겠습니까?')) return;
    try {
      await cancel(courseId);
      alert('수강 취소가 완료되었습니다.');
    } catch (err) {
      alert(`취소 실패: ${err?.message || err || '알 수 없는 오류'}`);
    }
  };

  const renderContent = () => {
    if (isLoading) return <div className="p-12 text-center text-gray-400">불러오는 중...</div>;
    if (error) return <div className="p-12 text-center text-red-400">오류 발생: {error}</div>;
    if (myRegistrations.length === 0) {
      return (
        <div className="p-12 text-center">
          <p className="text-gray-400 text-lg mb-4">신청한 강의가 없습니다.</p>
          <Button onClick={() => navigate('/')}>강의 신청하러 가기</Button>
        </div>
      );
    }
    return <MyRegistrationsTable registrations={myRegistrations} onCancel={handleCancel} />;
  };

  return (
    <div className="min-h-screen bg-gray-900 text-white p-8">
      <header className="max-w-6xl mx-auto flex justify-between items-center mb-12">
        <div>
          <h1 className="text-3xl font-bold">나의 수강 신청 현황</h1>
          <p className="text-gray-400">{user?.name}님의 신청 내역입니다.</p>
        </div>
        <Button variant="outline" onClick={() => navigate('/')}>
          뒤로 가기
        </Button>
      </header>

      <main className="max-w-6xl mx-auto">
        <div className="bg-gray-800 rounded-xl shadow-xl border border-gray-700 overflow-hidden">
          {renderContent()}
        </div>
      </main>
    </div>
  );
};

export default MyCoursesPage;
