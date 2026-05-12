import { create } from 'zustand';
import { courseApi } from '../api/courseApi';

export const useCourseStore = create((set, get) => ({
  courses: [],
  isLoading: false,
  error: null,
  myCourseIds: [], // IDs of courses the user has registered for
  
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
  
  registerCourse: (courseId) => set((state) => {
    // Check if already registered
    if (state.myCourseIds.includes(courseId)) {
      alert('이미 신청한 강의입니다.');
      return state;
    }
    
    return {
      courses: state.courses.map((c) => 
        c.id === courseId && c.currentEnrolled < c.maxCapacity 
          ? { ...c, currentEnrolled: c.currentEnrolled + 1 } 
          : c
      ),
      myCourseIds: [...state.myCourseIds, courseId]
    };
  }),
  
  cancelRegistration: (courseId) => set((state) => ({
    courses: state.courses.map((c) => 
      c.id === courseId ? { ...c, currentEnrolled: Math.max(0, c.currentEnrolled - 1) } : c
    ),
    myCourseIds: state.myCourseIds.filter(id => id !== courseId)
  })),
}));
