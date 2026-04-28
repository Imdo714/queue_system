import { Button } from '../../../components/shared/Button';
import { useCourses } from '../hooks/useCourses';
import { useQueue } from '../../queue/hooks/useQueue';
import { QueueModal } from '../../queue/components/QueueModal';

export const CourseList = () => {
  const { courses, register, myCourseIds } = useCourses();
  const { rank, joinQueue, cancelQueue } = useQueue();

  const handleRegister = (courseId) => {
    joinQueue(courseId, (id) => {
      register(id);
      alert('수강신청이 성공적으로 완료되었습니다!');
    });
  };

  return (
    <div className="overflow-x-auto">
      <QueueModal rank={rank} onCancel={cancelQueue} />
      <table className="w-full text-left border-collapse">
        <thead>
          <tr className="border-b border-gray-700 bg-gray-800">
            <th className="px-4 py-3 text-sm font-semibold text-gray-300">강의명</th>
            <th className="px-4 py-3 text-sm font-semibold text-gray-300">교수</th>
            <th className="px-4 py-3 text-sm font-semibold text-gray-300">학점</th>
            <th className="px-4 py-3 text-sm font-semibold text-gray-300">인원</th>
            <th className="px-4 py-3 text-sm font-semibold text-gray-300">상태</th>
            <th className="px-4 py-3 text-sm font-semibold text-gray-300 text-right">신청</th>
          </tr>
        </thead>
        <tbody className="divide-y divide-gray-700">
          {courses.map((course) => {
            const isRegistered = myCourseIds?.includes(course.id);
            const isFull = course.current >= course.capacity;

            return (
              <tr key={course.id} className="hover:bg-gray-800/50 transition-colors">
                <td className="px-4 py-4 text-white font-medium">{course.name}</td>
                <td className="px-4 py-4 text-gray-300">{course.professor}</td>
                <td className="px-4 py-4 text-gray-300">{course.credit}</td>
                <td className="px-4 py-4 text-gray-300">
                  {course.current} / {course.capacity}
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
                  <Button
                    size="sm"
                    variant={isRegistered ? 'outline' : isFull ? 'outline' : 'primary'}
                    disabled={isFull || isRegistered}
                    onClick={() => handleRegister(course.id)}
                  >
                    {isRegistered ? '신청완료' : '수강신청'}
                  </Button>
                </td>
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
};
