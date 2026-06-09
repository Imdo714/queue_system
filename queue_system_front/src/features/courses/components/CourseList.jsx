import { useEffect } from 'react';
import { useCourses } from '../hooks/useCourses';
import { useQueue } from '../../queue/hooks/useQueue';
import { useAuthStore } from '../../../store/useAuthStore';
import { QueueModal } from '../../queue/components/QueueModal';
import { CourseTableRow } from './CourseTableRow';

export const CourseList = () => {
  const { courses, getCourses, getMyRegistrations, myCourseIds, isLoading, error, deleteCourse } = useCourses();
  const { rank, totalInQueue, joinQueue, cancelQueue } = useQueue();
  const { user } = useAuthStore();

  const isAdmin = user?.role === 'ADMIN' || user?.studentNo === 'admin';

  useEffect(() => {
    getCourses();
  }, []);

  const handleRegister = (courseId) => {
    joinQueue(courseId, user?.studentNo, async () => {
      alert('수강신청이 완료되었습니다!');
      await Promise.all([getCourses(), getMyRegistrations()]);
    });
  };

  const handleDelete = async (courseId) => {
    if (!window.confirm('정말로 이 강의를 삭제하시겠습니까?')) return;
    const success = await deleteCourse(courseId, user?.studentNo || 'admin');
    alert(success ? '강의가 삭제되었습니다.' : '강의 삭제에 실패했습니다.');
  };

  if (isLoading) return <div className="text-center py-10 text-gray-400">강의 정보를 불러오는 중...</div>;
  if (error) return <div className="text-center py-10 text-red-400">에러 발생: {error}</div>;

  return (
    <div className="overflow-x-auto">
      <QueueModal rank={rank} totalInQueue={totalInQueue} onCancel={cancelQueue} />
      <table className="w-full text-left border-collapse">
        <thead>
          <tr className="border-b border-gray-700 bg-gray-800">
            {['코드', '강의명', '시간', '인원', '상태', '관리/신청'].map((header, i) => (
              <th
                key={header}
                className={`px-4 py-3 text-sm font-semibold text-gray-300 ${i === 5 ? 'text-right' : ''}`}
              >
                {header}
              </th>
            ))}
          </tr>
        </thead>
        <tbody className="divide-y divide-gray-700">
          {courses.length === 0 ? (
            <tr>
              <td colSpan="6" className="px-4 py-10 text-center text-gray-500">
                등록된 강의가 없습니다.
              </td>
            </tr>
          ) : (
            courses.map((course) => (
              <CourseTableRow
                key={course.id}
                course={course}
                isRegistered={myCourseIds?.includes(course.id)}
                isAdmin={isAdmin}
                onRegister={handleRegister}
                onDelete={handleDelete}
              />
            ))
          )}
        </tbody>
      </table>
    </div>
  );
};
