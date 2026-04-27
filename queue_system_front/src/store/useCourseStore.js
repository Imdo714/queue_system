import { create } from 'zustand';

export const useCourseStore = create((set) => ({
  courses: [
    { id: 1, name: '자바스크립트 프로그래밍', professor: '김철수', credit: 3, capacity: 30, current: 15 },
    { id: 2, name: '리액트 심화과정', professor: '이영희', credit: 3, capacity: 20, current: 20 },
    { id: 3, name: '알고리즘 및 자료구조', professor: '박지민', credit: 4, capacity: 40, current: 35 },
  ],
  myCourseIds: [], // IDs of courses the user has registered for
  
  addCourse: (course) => set((state) => ({ 
    courses: [...state.courses, { ...course, id: Date.now(), current: 0 }] 
  })),
  
  registerCourse: (courseId) => set((state) => {
    // Check if already registered
    if (state.myCourseIds.includes(courseId)) {
      alert('이미 신청한 강의입니다.');
      return state;
    }
    
    return {
      courses: state.courses.map((c) => 
        c.id === courseId && c.current < c.capacity 
          ? { ...c, current: c.current + 1 } 
          : c
      ),
      myCourseIds: [...state.myCourseIds, courseId]
    };
  }),
  
  cancelRegistration: (courseId) => set((state) => ({
    courses: state.courses.map((c) => 
      c.id === courseId ? { ...c, current: Math.max(0, c.current - 1) } : c
    ),
    myCourseIds: state.myCourseIds.filter(id => id !== courseId)
  })),
}));
