import { useCourseStore } from '../../../store/useCourseStore';

export const useCourses = () => {
  const { 
    courses, 
    myCourseIds, 
    addCourse: storeAddCourse, 
    registerCourse: storeRegisterCourse,
    cancelRegistration: storeCancelRegistration 
  } = useCourseStore();

  const getCourses = async () => {
    // Future API call: return await api.getCourses();
    return courses;
  };

  const register = async (courseId) => {
    // Future API call: await api.register(courseId);
    storeRegisterCourse(courseId);
    return true;
  };

  const cancel = async (courseId) => {
    // Future API call: await api.cancel(courseId);
    storeCancelRegistration(courseId);
    return true;
  };

  const createCourse = async (courseData) => {
    // Future API call: await api.createCourse(courseData);
    storeAddCourse(courseData);
    return true;
  };

  const myCourses = courses.filter(course => myCourseIds.includes(course.id));

  return {
    courses,
    myCourses,
    getCourses,
    register,
    cancel,
    createCourse,
  };
};
