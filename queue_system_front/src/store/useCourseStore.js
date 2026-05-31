import { create } from 'zustand';
import { courseApi } from '../api/courseApi';

export const useCourseStore = create((set, get) => ({
  courses: [],
  myRegistrations: [], // GET /registration 응답 목록 (RegistrationResponse[])
  isLoading: false,
  error: null,
  myCourseIds: [],
  
  fetchCourses: async () => {
    set({ isLoading: true });
    try {
      const courses = await courseApi.getCourses();
      set({ courses, isLoading: false, error: null });
    } catch (error) {
      set({ error, isLoading: false });
    }
  },

  // 강의 생성
  createCourse: async (courseData) => {
    set({ isLoading: true });
    try {
      await courseApi.createCourse(courseData);
      await get().fetchCourses(); // Refresh list after creation
      set({ isLoading: false, error: null });
      return true;
    } catch (error) {
      set({ error, isLoading: false });
      return false;
    }
  },

  deleteCourse: async (courseId, studentNo) => {
    set({ isLoading: true });
    try {
      await courseApi.deleteCourse(courseId, studentNo);
      set((state) => ({
        courses: state.courses.filter((c) => c.id !== courseId),
        isLoading: false,
        error: null
      }));
      return true;
    } catch (error) {
      set({ error, isLoading: false });
      return false;
    }
  },
  
  registerCourse: async (courseId, studentNo) => {
    if (get().myCourseIds.includes(courseId)) {
      throw new Error('이미 신청한 강의입니다.');
    }
    await courseApi.registerCourse(courseId, studentNo);
    set((state) => ({
      courses: state.courses.map((c) =>
        c.id === courseId && c.currentEnrolled < c.maxCapacity
          ? { ...c, currentEnrolled: c.currentEnrolled + 1 }
          : c
      ),
      myCourseIds: [...state.myCourseIds, courseId],
    }));
  },

  cancelRegistration: async (courseId, studentNo) => {
    await courseApi.cancelRegistration(courseId, studentNo);
    set((state) => ({
      courses: state.courses.map((c) =>
        c.id === courseId ? { ...c, currentEnrolled: Math.max(0, c.currentEnrolled - 1) } : c
      ),
      myCourseIds: state.myCourseIds.filter((id) => id !== courseId),
      myRegistrations: state.myRegistrations.filter((r) => r.courseId !== courseId),
    }));
  },

  fetchMyRegistrations: async (studentNo) => {
    set({ isLoading: true });
    try {
      const registrations = await courseApi.getMyRegistrations(studentNo);
      // 로컬 myCourseIds도 동기화하여 CourseList의 "신청됨" 뱃지와 일치시킴
      const ids = registrations.map((r) => r.courseId);
      set({ myRegistrations: registrations, myCourseIds: ids, isLoading: false, error: null });
    } catch (error) {
      set({ error, isLoading: false });
    }
  },
}));
