import { Trash2 } from 'lucide-react';
import { Button } from '../../../components/shared/Button';
import { CourseStatusBadge } from './CourseStatusBadge';

export const CourseTableRow = ({ course, isRegistered, isAdmin, onRegister, onDelete }) => {
  const isFull = course.currentEnrolled >= course.maxCapacity;

  return (
    <tr className="hover:bg-gray-800/50 transition-colors">
      <td className="px-4 py-4 text-gray-400 font-mono text-sm">{course.courseCode}</td>
      <td className="px-4 py-4 text-white font-medium">{course.title}</td>
      <td className="px-4 py-4 text-gray-300 text-sm">
        {course.dayOfWeek} {course.startTime.substring(0, 5)} - {course.endTime.substring(0, 5)}
      </td>
      <td className="px-4 py-4 text-gray-300">
        {course.currentEnrolled} / {course.maxCapacity}
      </td>
      <td className="px-4 py-4">
        <CourseStatusBadge isRegistered={isRegistered} isFull={isFull} />
      </td>
      <td className="px-4 py-4 text-right">
        <div className="flex justify-end gap-2">
          {isAdmin && (
            <Button
              size="sm"
              variant="danger"
              onClick={() => onDelete(course.id)}
              className="flex items-center gap-1"
            >
              <Trash2 size={14} />
              삭제
            </Button>
          )}
          <Button
            size="sm"
            variant={isRegistered || isFull ? 'outline' : 'primary'}
            disabled={isFull || isRegistered}
            onClick={() => onRegister(course.id)}
          >
            {isRegistered ? '신청완료' : '수강신청'}
          </Button>
        </div>
      </td>
    </tr>
  );
};
