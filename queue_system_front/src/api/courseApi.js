import api from './axios';

export const courseApi = {
  // Get all courses
  getCourses: () => api.get('/course'),

  // Get specific course
  getCourse: (courseId) => api.get(`/course/${courseId}`),

  // Create course (Admin)
  createCourse: (courseData) => api.post('/admin/course', courseData),

  // Delete course (Admin)
  deleteCourse: (courseId, studentNo) => api.delete(`/admin/course/${courseId}`, {
    data: { studentNo }
  }),
};
