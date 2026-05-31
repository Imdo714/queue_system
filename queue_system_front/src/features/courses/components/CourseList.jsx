import { useEffect } from 'react';
import { Button } from '../../../components/shared/Button';
import { useCourses } from '../hooks/useCourses';
import { useQueue } from '../../queue/hooks/useQueue';
import { QueueModal } from '../../queue/components/QueueModal';
import { useAuthStore } from '../../../store/useAuthStore';
import { Trash2 } from 'lucide-react';

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
    if (window.confirm('정말로 이 강의를 삭제하시겠습니까?')) {
      const success = await deleteCourse(courseId, user?.studentNo || 'admin');
      if (success) {
        alert('강의가 삭제되었습니다.');
      } else {
        alert('강의 삭제에 실패했습니다.');
      }
    }
  };

  if (isLoading) return <div className="text-center py-10 text-gray-400">강의 정보를 불러오는 중...</div>;
  if (error) return <div className="text-center py-10 text-red-400">에러 발생: {error}</div>;

  return (
    <div className="overflow-x-auto">
      <QueueModal rank={rank} totalInQueue={totalInQueue} onCancel={cancelQueue} />
      <table className="w-full text-left border-collapse">
        <thead>
          <tr className="border-b border-gray-700 bg-gray-800">
            <th className="px-4 py-3 text-sm font-semibold text-gray-300">코드</th>
            <th className="px-4 py-3 text-sm font-semibold text-gray-300">강의명</th>
            <th className="px-4 py-3 text-sm font-semibold text-gray-300">시간</th>
            <th className="px-4 py-3 text-sm font-semibold text-gray-300">인원</th>
            <th className="px-4 py-3 text-sm font-semibold text-gray-300">상태</th>
            <th className="px-4 py-3 text-sm font-semibold text-gray-300 text-right">관리/신청</th>
          </tr>
        </thead>
        <tbody className="divide-y divide-gray-700">
          {courses.length === 0 ? (
            <tr>
              <td colSpan="6" className="px-4 py-10 text-center text-gray-500">등록된 강의가 없습니다.</td>
            </tr>
          ) : (
            courses.map((course) => {
              const isRegistered = myCourseIds?.includes(course.id);
              const isFull = course.currentEnrolled >= course.maxCapacity;

              return (
                <tr key={course.id} className="hover:bg-gray-800/50 transition-colors">
                  <td className="px-4 py-4 text-gray-400 font-mono text-sm">{course.courseCode}</td>
                  <td className="px-4 py-4 text-white font-medium">{course.title}</td>
                  <td className="px-4 py-4 text-gray-300 text-sm">
                    {course.dayOfWeek} {course.startTime.substring(0, 5)} - {course.endTime.substring(0, 5)}
                  </td>
                  <td className="px-4 py-4 text-gray-300">
                    {course.currentEnrolled} / {course.maxCapacity}
                  </td>
                  <td className="px-4 py-4">
                    {isRegistered ? (
                      <span className="px-2 py-1 text-xs rounded-full bg-blue-900/30 text-blue-400 border border-blue-800">
                        신청됨
                      </span>
                    ) : isFull ? (
                      <span className="px-2 py-1 text-xs rounded-full bg-red-900/30 text-red-400 border border-red-800">
                        마감
                      </span>
                    ) : (
                      <span className="px-2 py-1 text-xs rounded-full bg-green-900/30 text-green-400 border border-green-800">
                        가능
                      </span>
                    )}
                  </td>
                  <td className="px-4 py-4 text-right">
                    <div className="flex justify-end gap-2">
                      {isAdmin && (
                        <Button
                          size="sm"
                          variant="danger"
                          onClick={() => handleDelete(course.id)}
                          className="flex items-center gap-1"
                        >
                          <Trash2 size={14} />
                          삭제
                        </Button>
                      )}
                      <Button
                        size="sm"
                        variant={isRegistered ? 'outline' : isFull ? 'outline' : 'primary'}
                        disabled={isFull || isRegistered}
                        onClick={() => handleRegister(course.id)}
                      >
                        {isRegistered ? '신청완료' : '수강신청'}
                      </Button>
                    </div>
                  </td>
                </tr>
              );
            })
          )}
        </tbody>
      </table>
    </div>
  );
};
