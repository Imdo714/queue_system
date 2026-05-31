import api from './axios';

export const courseApi = {
  getCourses: () => api.get('/course'),

  getCourse: (courseId) => api.get(`/course/${courseId}`),

  createCourse: (courseData) => api.post('/admin/course', courseData),

  deleteCourse: (courseId, studentNo) => api.delete(`/admin/course/${courseId}`, {
    data: { studentNo }
  }),

  registerCourse: (courseId, studentNo) =>
    api.post('/registration', { studentNo, courseId }),

  cancelRegistration: (courseId, studentNo) =>
    api.delete(`/registration/${courseId}`, { data: { studentNo } }),

  getMyRegistrations: (studentNo) =>
    api.get('/registration', { params: { studentNo } }),

  getQueueRank: (courseId, studentNo) =>
    api.get(`/registration/queue/${courseId}/rank`, { params: { studentNo } }),
};
