import { useCourseStore } from '../../../store/useCourseStore';
import { useAuthStore } from '../../../store/useAuthStore';

export const useCourses = () => {
  const {
    courses,
    myRegistrations,
    myCourseIds,
    isLoading,
    error,
    fetchCourses,
    fetchMyRegistrations: storeFetchMyRegistrations,
    createCourse: storeCreateCourse,
    deleteCourse: storeDeleteCourse,
    registerCourse: storeRegisterCourse,
    cancelRegistration: storeCancelRegistration
  } = useCourseStore();

  const { user } = useAuthStore();

  const getCourses = async () => {
    await fetchCourses();
  };

  const register = async (courseId) => {
    await storeRegisterCourse(courseId, user?.studentNo);
    return true;
  };

  const cancel = async (courseId) => {
    await storeCancelRegistration(courseId, user?.studentNo);
    return true;
  };

  const createCourse = async (courseData) => {
    return await storeCreateCourse(courseData);
  };

  const deleteCourse = async (courseId, studentNo) => {
    return await storeDeleteCourse(courseId, studentNo);
  };

  const getMyRegistrations = async () => {
    await storeFetchMyRegistrations(user?.studentNo);
  };

  const myCourses = courses.filter(course => myCourseIds.includes(course.id));

  return {
    courses,
    myCourses,
    myRegistrations,
    isLoading,
    error,
    getCourses,
    getMyRegistrations,
    register,
    cancel,
    createCourse,
    deleteCourse,
  };
};
