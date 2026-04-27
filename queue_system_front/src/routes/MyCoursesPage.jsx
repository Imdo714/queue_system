import { useNavigate } from 'react-router-dom';
import { Button } from '../components/shared/Button';
import { useCourses } from '../features/courses/hooks/useCourses';
import { useAuth } from '../features/auth/hooks/useAuth';

const MyCoursesPage = () => {
  const { myCourses, cancel } = useCourses();
  const { user } = useAuth();
  const navigate = useNavigate();

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
          {myCourses.length === 0 ? (
            <div className="p-12 text-center">
              <p className="text-gray-400 text-lg mb-4">신청한 강의가 없습니다.</p>
              <Button onClick={() => navigate('/')}>강의 신청하러 가기</Button>
            </div>
          ) : (
            <table className="w-full text-left border-collapse">
              <thead>
                <tr className="border-b border-gray-700 bg-gray-800">
                  <th className="px-4 py-3 text-sm font-semibold text-gray-300">강의명</th>
                  <th className="px-4 py-3 text-sm font-semibold text-gray-300">교수</th>
                  <th className="px-4 py-3 text-sm font-semibold text-gray-300">학점</th>
                  <th className="px-4 py-3 text-sm font-semibold text-gray-300 text-right">취소</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-700">
                {myCourses.map((course) => (
                  <tr key={course.id} className="hover:bg-gray-800/50 transition-colors">
                    <td className="px-4 py-4 text-white font-medium">{course.name}</td>
                    <td className="px-4 py-4 text-gray-300">{course.professor}</td>
                    <td className="px-4 py-4 text-gray-300">{course.credit}</td>
                    <td className="px-4 py-4 text-right">
                      <Button
                        size="sm"
                        variant="danger"
                        onClick={() => cancel(course.id)}
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
