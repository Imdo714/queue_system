import { useState, useCallback, useRef } from 'react';

// 대기열 설정: 5명을 200ms 간격으로 차감 → 정확히 1초 후 완료
const INITIAL_RANK = 5;
const INTERVAL_MS = 200; // 5 × 200ms = 1,000ms

export const useQueue = () => {
  const [isWaiting, setIsWaiting] = useState(false);
  const [rank, setRank] = useState(null);
  const [currentCourseId, setCurrentCourseId] = useState(null);

  const intervalRef = useRef(null);
  const onCompleteRef = useRef(null);
  const isCancelledRef = useRef(false);

  const joinQueue = useCallback((courseId, onComplete) => {
    if (intervalRef.current) clearInterval(intervalRef.current);

    isCancelledRef.current = false;
    onCompleteRef.current = onComplete;
    setCurrentCourseId(courseId);
    setIsWaiting(true);
    setRank(INITIAL_RANK);

    let current = INITIAL_RANK;

    intervalRef.current = setInterval(() => {
      if (isCancelledRef.current) {
        clearInterval(intervalRef.current);
        intervalRef.current = null;
        return;
      }

      current -= 1;
      setRank(current);

      if (current <= 0) {
        clearInterval(intervalRef.current);
        intervalRef.current = null;
        setIsWaiting(false);
        setRank(null);
        setCurrentCourseId(null);

        // 단방향 소비 — 중복 호출 방지
        const cb = onCompleteRef.current;
        onCompleteRef.current = null;
        if (cb) cb(courseId);
      }
    }, INTERVAL_MS);
  }, []);

  const cancelQueue = useCallback(() => {
    isCancelledRef.current = true;
    if (intervalRef.current) {
      clearInterval(intervalRef.current);
      intervalRef.current = null;
    }
    onCompleteRef.current = null;
    setIsWaiting(false);
    setRank(null);
    setCurrentCourseId(null);
  }, []);

  return { isWaiting, rank, currentCourseId, joinQueue, cancelQueue };
};
