import { Button } from '../../../components/shared/Button';

export const QueueModal = ({ rank, onCancel }) => {
  if (rank === null) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/60 backdrop-blur-sm">
      <div className="bg-gray-900 border border-gray-700 p-8 rounded-2xl shadow-2xl max-w-sm w-full text-center">
        <div className="mb-6">
          <div className="inline-flex items-center justify-center w-16 h-16 rounded-full bg-blue-900/30 text-blue-400 mb-4 animate-pulse">
            <svg xmlns="http://www.w3.org/2000/svg" className="h-8 w-8" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
          </div>
          <h3 className="text-2xl font-bold text-white mb-2">대기 중입니다</h3>
          <p className="text-gray-400">접속 인원이 많아 대기열에 진입했습니다.<br />잠시만 기다려 주세요.</p>
        </div>

        <div className="bg-gray-800 rounded-xl p-6 mb-8 border border-gray-700">
          <p className="text-sm text-gray-400 uppercase tracking-wider mb-1">내 대기 번호</p>
          <p className="text-5xl font-black text-blue-500 tabular-nums">
            {rank}
          </p>
        </div>

        <div className="space-y-3">
          <p className="text-xs text-gray-500">
            * 창을 닫거나 새로고침하면 대기열에서 이탈하게 됩니다.
          </p>
          <Button 
            variant="outline" 
            className="w-full border-gray-700 text-gray-400 hover:bg-red-900/20 hover:text-red-400 hover:border-red-900/50 transition-all"
            onClick={onCancel}
          >
            대기 취소하기
          </Button>
        </div>
      </div>
    </div>
  );
};
