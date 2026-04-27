import { useState } from 'react';
import { Button } from '../../../components/shared/Button';
import { Input } from '../../../components/shared/Input';
import { useCourses } from '../hooks/useCourses';

export const CourseRegistration = () => {
  const [isOpen, setIsOpen] = useState(false);
  const [formData, setFormData] = useState({
    name: '',
    professor: '',
    credit: 3,
    capacity: 30,
  });
  const { createCourse } = useCourses();

  const handleSubmit = async (e) => {
    e.preventDefault();
    await createCourse(formData);
    setIsOpen(false);
    setFormData({ name: '', professor: '', credit: 3, capacity: 30 });
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
          label="강의명"
          value={formData.name}
          onChange={(e) => setFormData({ ...formData, name: e.target.value })}
          required
        />
        <Input
          label="교수명"
          value={formData.professor}
          onChange={(e) => setFormData({ ...formData, professor: e.target.value })}
          required
        />
        <Input
          label="학점"
          type="number"
          value={formData.credit}
          onChange={(e) => setFormData({ ...formData, credit: parseInt(e.target.value) })}
          required
        />
        <Input
          label="수강 정원"
          type="number"
          value={formData.capacity}
          onChange={(e) => setFormData({ ...formData, capacity: parseInt(e.target.value) })}
          required
        />
        <div className="md:col-span-2 flex justify-end gap-2 mt-4">
          <Button type="button" variant="outline" onClick={() => setIsOpen(false)}>
            취소
          </Button>
          <Button type="submit">
            등록
          </Button>
        </div>
      </form>
    </div>
  );
};
