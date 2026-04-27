import { Button } from '../../../components/shared/Button';
import { useCourses } from '../hooks/useCourses';

export const CourseList = () => {
  const { courses, register } = useCourses();

  return (
    <div className="overflow-x-auto">
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
          {courses.map((course) => (
            <tr key={course.id} className="hover:bg-gray-800/50 transition-colors">
              <td className="px-4 py-4 text-white font-medium">{course.name}</td>
              <td className="px-4 py-4 text-gray-300">{course.professor}</td>
              <td className="px-4 py-4 text-gray-300">{course.credit}</td>
              <td className="px-4 py-4 text-gray-300">
                {course.current} / {course.capacity}
              </td>
              <td className="px-4 py-4">
                {course.current >= course.capacity ? (
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
                  variant={course.current >= course.capacity ? 'outline' : 'primary'}
                  disabled={course.current >= course.capacity}
                  onClick={() => register(course.id)}
                >
                  수강신청
                </Button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};
