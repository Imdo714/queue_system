import { useState, useCallback } from 'react';
import { courseApi } from '../../../api/courseApi';

export const useQueue = () => {
  const [isWaiting, setIsWaiting] = useState(false);
  const [rank, setRank] = useState(null);
  const [totalInQueue, setTotalInQueue] = useState(null);
  const [currentCourseId, setCurrentCourseId] = useState(null);

  /**
   * 수강신청 진입점.
   * Redis Lua가 정원 체크 + 등록을 원자적으로 처리하므로 항상 즉시 응답.
   *  - 성공    → onComplete() 즉시 호출
   *  - 정원 초과 / 중복 → alert
   */
  const joinQueue = useCallback(async (courseId, studentNo, onComplete) => {
    try {
      await courseApi.registerCourse(courseId, studentNo);
      if (onComplete) onComplete(courseId);
    } catch (err) {
      alert(typeof err === 'string' ? err : '수강신청에 실패했습니다.');
    }
  }, []);

  // 사용하지 않지만 QueueModal / CourseList 인터페이스 호환성 유지
  const cancelQueue = useCallback(() => {
    setIsWaiting(false);
    setRank(null);
    setTotalInQueue(null);
    setCurrentCourseId(null);
  }, []);

  return { isWaiting, rank, totalInQueue, currentCourseId, joinQueue, cancelQueue };
};
