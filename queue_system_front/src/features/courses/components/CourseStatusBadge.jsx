const BADGE_STYLES = {
  registered: 'bg-blue-900/30 text-blue-400 border-blue-800',
  full: 'bg-red-900/30 text-red-400 border-red-800',
  available: 'bg-green-900/30 text-green-400 border-green-800',
};

const BADGE_LABELS = {
  registered: '신청됨',
  full: '마감',
  available: '가능',
};

export const CourseStatusBadge = ({ isRegistered, isFull }) => {
  const type = isRegistered ? 'registered' : isFull ? 'full' : 'available';

  return (
    <span className={`px-2 py-1 text-xs rounded-full border ${BADGE_STYLES[type]}`}>
      {BADGE_LABELS[type]}
    </span>
  );
};
