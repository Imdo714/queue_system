import { useState, useCallback, useRef } from 'react';

export const useQueue = () => {
  const [isWaiting, setIsWaiting] = useState(false);
  const [rank, setRank] = useState(null);
  const [currentCourseId, setCurrentCourseId] = useState(null);
  const intervalRef = useRef(null);

  const joinQueue = useCallback(async (courseId, onComplete) => {
    setCurrentCourseId(courseId);
    
    const shouldWait = Math.random() > 0.3;

    if (!shouldWait) {
      if (onComplete) onComplete(courseId);
      return;
    }

    setIsWaiting(true);
    let currentRank = Math.floor(Math.random() * 50);
    setRank(currentRank);

    if (intervalRef.current) clearInterval(intervalRef.current);

    intervalRef.current = setInterval(() => {
      setRank((prev) => {
        if (prev === null) return null;
        if (prev <= 1) {
          clearInterval(intervalRef.current);
          intervalRef.current = null;
          setIsWaiting(false);
          setRank(null);
          if (onComplete) onComplete(courseId);
          return null;
        }
        return prev - 1;
      });
    }, 1000);
  }, []);

  const cancelQueue = useCallback(() => {
    if (intervalRef.current) {
      clearInterval(intervalRef.current);
      intervalRef.current = null;
    }
    setIsWaiting(false);
    setRank(null);
    setCurrentCourseId(null);
  }, []);

  return {
    isWaiting,
    rank,
    currentCourseId,
    joinQueue,
    cancelQueue,
  };
};
