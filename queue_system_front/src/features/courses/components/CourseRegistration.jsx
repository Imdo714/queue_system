import { useState } from 'react';
import { Button } from '../../../components/shared/Button';
import { Input } from '../../../components/shared/Input';
import { useCourses } from '../hooks/useCourses';
import { useAuthStore } from '../../../store/useAuthStore';

export const CourseRegistration = () => {
  const [isOpen, setIsOpen] = useState(false);
  const { user } = useAuthStore();
  const [formData, setFormData] = useState({
    courseCode: '',
    title: '',
    maxCapacity: 30,
    dayOfWeek: 'MONDAY',
    startTime: '09:00:00',
    endTime: '11:00:00',
  });
  
  const { createCourse, isLoading } = useCourses();

  const handleSubmit = async (e) => {
    e.preventDefault();
    const success = await createCourse({
      ...formData,
      studentNo: user?.studentNo || 'admin', // Default to admin for now if not logged in
    });
    
    if (success) {
      setIsOpen(false);
      setFormData({
        courseCode: '',
        title: '',
        maxCapacity: 30,
        dayOfWeek: 'MONDAY',
        startTime: '09:00:00',
        endTime: '11:00:00',
      });
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
          onChange={(e) => setFormData({ ...formData, courseCode: e.target.value })}
          required
        />
        <Input
          label="강의명"
          value={formData.title}
          onChange={(e) => setFormData({ ...formData, title: e.target.value })}
          required
        />
        <Input
          label="수강 정원"
          type="number"
          value={formData.maxCapacity}
          onChange={(e) => setFormData({ ...formData, maxCapacity: parseInt(e.target.value) })}
          required
        />
        <div>
          <label className="block text-sm font-medium text-gray-400 mb-1">요일</label>
          <select 
            className="w-full bg-gray-900 border border-gray-700 rounded-lg px-4 py-2 text-white focus:outline-none focus:ring-2 focus:ring-blue-500"
            value={formData.dayOfWeek}
            onChange={(e) => setFormData({ ...formData, dayOfWeek: e.target.value })}
          >
            {['MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY'].map(day => (
              <option key={day} value={day}>{day}</option>
            ))}
          </select>
        </div>
        <Input
          label="시작 시간"
          type="time"
          step="1"
          value={formData.startTime.substring(0, 5)}
          onChange={(e) => setFormData({ ...formData, startTime: e.target.value + ':00' })}
          required
        />
        <Input
          label="종료 시간"
          type="time"
          step="1"
          value={formData.endTime.substring(0, 5)}
          onChange={(e) => setFormData({ ...formData, endTime: e.target.value + ':00' })}
          required
        />
        <div className="md:col-span-2 flex justify-end gap-2 mt-4">
          <Button type="button" variant="outline" onClick={() => setIsOpen(false)}>
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
