import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Button } from '../components/shared/Button';
import { useCourses } from '../features/courses/hooks/useCourses';
import { useAuth } from '../features/auth/hooks/useAuth';

const DAY_KO = {
  MONDAY: '월', TUESDAY: '화', WEDNESDAY: '수',
  THURSDAY: '목', FRIDAY: '금', SATURDAY: '토', SUNDAY: '일',
};

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
          {isLoading ? (
            <div className="p-12 text-center text-gray-400">불러오는 중...</div>
          ) : error ? (
            <div className="p-12 text-center text-red-400">오류 발생: {error}</div>
          ) : myRegistrations.length === 0 ? (
            <div className="p-12 text-center">
              <p className="text-gray-400 text-lg mb-4">신청한 강의가 없습니다.</p>
              <Button onClick={() => navigate('/')}>강의 신청하러 가기</Button>
            </div>
          ) : (
            <table className="w-full text-left border-collapse">
              <thead>
                <tr className="border-b border-gray-700 bg-gray-800">
                  <th className="px-4 py-3 text-sm font-semibold text-gray-300">코드</th>
                  <th className="px-4 py-3 text-sm font-semibold text-gray-300">강의명</th>
                  <th className="px-4 py-3 text-sm font-semibold text-gray-300">시간</th>
                  <th className="px-4 py-3 text-sm font-semibold text-gray-300">신청일</th>
                  <th className="px-4 py-3 text-sm font-semibold text-gray-300 text-right">취소</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-700">
                {myRegistrations.map((reg) => (
                  <tr key={reg.id} className="hover:bg-gray-800/50 transition-colors">
                    <td className="px-4 py-4 text-gray-400 font-mono text-sm">{reg.courseCode}</td>
                    <td className="px-4 py-4 text-white font-medium">{reg.title}</td>
                    <td className="px-4 py-4 text-gray-300 text-sm">
                      {DAY_KO[reg.dayOfWeek] ?? reg.dayOfWeek}{' '}
                      {String(reg.startTime).substring(0, 5)} - {String(reg.endTime).substring(0, 5)}
                    </td>
                    <td className="px-4 py-4 text-gray-400 text-sm">
                      {reg.registrationDate
                        ? new Date(reg.registrationDate).toLocaleDateString('ko-KR')
                        : '-'}
                    </td>
                    <td className="px-4 py-4 text-right">
                      <Button
                        size="sm"
                        variant="danger"
                        onClick={() => handleCancel(reg.courseId)}
                      >
                        신청 취소
                      </Button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      </main>
    </div>
  );
};

export default MyCoursesPage;
