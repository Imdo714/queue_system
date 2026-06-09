import { useState } from 'react';
import { Button } from '../../../components/shared/Button';
import { Input } from '../../../components/shared/Input';
import { useCourses } from '../hooks/useCourses';
import { useAuthStore } from '../../../store/useAuthStore';

const INITIAL_FORM = {
  courseCode: '',
  title: '',
  maxCapacity: 30,
  dayOfWeek: 'MONDAY',
  startTime: '09:00:00',
  endTime: '11:00:00',
};

const DAYS = ['MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY'];

const DAY_KO = {
  MONDAY: '월요일', TUESDAY: '화요일', WEDNESDAY: '수요일',
  THURSDAY: '목요일', FRIDAY: '금요일', SATURDAY: '토요일', SUNDAY: '일요일',
};

const toTimeValue = (time) => String(time).substring(0, 5);
const toTimeParam = (value) => value + ':00';

export const CourseRegistration = () => {
  const [isOpen, setIsOpen] = useState(false);
  const [formData, setFormData] = useState(INITIAL_FORM);
  const { user } = useAuthStore();
  const { createCourse, isLoading } = useCourses();

  const isAdmin = user?.studentNo === 'admin';
  if (!isAdmin) return null;

  const handleChange = (field, value) =>
    setFormData((prev) => ({ ...prev, [field]: value }));

  const handleClose = () => {
    setIsOpen(false);
    setFormData(INITIAL_FORM);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const success = await createCourse({ ...formData, studentNo: user?.studentNo || 'admin' });
    if (success) {
      handleClose();
      alert('강의가 성공적으로 등록되었습니다.');
    } else {
      alert('강의 등록에 실패했습니다.');
    }
  };

  if (!isOpen) {
    return (
      <Button onClick={() => setIsOpen(true)} variant="secondary">
        새 강의 등록
      </Button>
    );
  }

  return (
    <div className="mb-8 p-6 bg-gray-800 rounded-lg border border-gray-700">
      <h3 className="text-xl font-bold text-white mb-4">새 강의 추가</h3>
      <form onSubmit={handleSubmit} className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <Input
          label="강의코드"
          placeholder="e.g. CS101"
          value={formData.courseCode}
          onChange={(e) => handleChange('courseCode', e.target.value)}
          required
        />
        <Input
          label="강의명"
          value={formData.title}
          onChange={(e) => handleChange('title', e.target.value)}
          required
        />
        <Input
          label="수강 정원"
          type="number"
          value={formData.maxCapacity}
          onChange={(e) => handleChange('maxCapacity', parseInt(e.target.value))}
          required
        />
        <div>
          <label className="block text-sm font-medium text-gray-400 mb-1">요일</label>
          <select
            className="w-full bg-gray-900 border border-gray-700 rounded-lg px-4 py-2 text-white focus:outline-none focus:ring-2 focus:ring-blue-500"
            value={formData.dayOfWeek}
            onChange={(e) => handleChange('dayOfWeek', e.target.value)}
          >
            {DAYS.map((day) => (
              <option key={day} value={day}>{DAY_KO[day]}</option>
            ))}
          </select>
        </div>
        <Input
          label="시작 시간"
          type="time"
          value={toTimeValue(formData.startTime)}
          onChange={(e) => handleChange('startTime', toTimeParam(e.target.value))}
          required
        />
        <Input
          label="종료 시간"
          type="time"
          value={toTimeValue(formData.endTime)}
          onChange={(e) => handleChange('endTime', toTimeParam(e.target.value))}
          required
        />
        <div className="md:col-span-2 flex justify-end gap-2 mt-4">
          <Button type="button" variant="outline" onClick={handleClose}>
            취소
          </Button>
          <Button type="submit" disabled={isLoading}>
            {isLoading ? '등록 중...' : '등록'}
          </Button>
        </div>
      </form>
    </div>
  );
};
