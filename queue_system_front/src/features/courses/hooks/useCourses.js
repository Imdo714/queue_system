import { useEffect } from 'react';
import { useCourseStore } from '../../../store/useCourseStore';

export const useCourses = () => {
  const { 
    courses, 
    myCourseIds, 
    isLoading,
    error,
    fetchCourses,
    createCourse: storeCreateCourse,
    deleteCourse: storeDeleteCourse,
    registerCourse: storeRegisterCourse,
    cancelRegistration: storeCancelRegistration 
  } = useCourseStore();

  // Optionally fetch courses when the hook is used
  // Or let the component decide when to call getCourses
  const getCourses = async () => {
    await fetchCourses();
  };

  const register = async (courseId) => {
    // Note: Registration API not yet implemented on backend based on user request
    storeRegisterCourse(courseId);
    return true;
  };

  const cancel = async (courseId) => {
    // Note: Cancel API not yet implemented on backend
    storeCancelRegistration(courseId);
    return true;
  };

  const createCourse = async (courseData) => {
    return await storeCreateCourse(courseData);
  };

  const deleteCourse = async (courseId, studentNo) => {
    return await storeDeleteCourse(courseId, studentNo);
  };

  const myCourses = courses.filter(course => myCourseIds.includes(course.id));

  return {
    courses,
    myCourses,
    isLoading,
    error,
    getCourses,
    register,
    cancel,
    createCourse,
    deleteCourse,
  };
};
