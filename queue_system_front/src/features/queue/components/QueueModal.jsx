import { Button } from '../../../components/shared/Button';

export const QueueModal = ({ rank, onCancel }) => {
  if (rank === null) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/60 backdrop-blur-sm">
      <div className="bg-gray-900 border border-gray-700 p-8 rounded-2xl shadow-2xl max-w-sm w-full text-center">
        <div className="mb-6">
          <div className="inline-flex items-center justify-center w-16 h-16 rounded-full bg-blue-900/30 text-blue-400 mb-4 animate-pulse">
            <svg xmlns="http://www.w3.org/2000/svg" className="h-8 w-8" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0z" />
            </svg>
          </div>
          <h3 className="text-2xl font-bold text-white mb-2">대기 중입니다</h3>
          <p className="text-gray-400 text-sm">접속 인원이 많아 대기열에 진입했습니다.<br />순서가 되면 자동으로 신청이 완료됩니다.</p>
        </div>

        <div className="bg-gray-800 rounded-xl p-6 mb-6 border border-gray-700">
          <p className="text-sm text-gray-400 uppercase tracking-wider mb-3">내 앞에 남은 인원</p>
          <div className="flex items-end justify-center gap-2">
            <p className="text-6xl font-black text-blue-400 tabular-nums leading-none">
              {rank}
            </p>
            <p className="text-xl text-gray-300 mb-1">명</p>
          </div>
          <p className="text-xs text-gray-500 mt-3">순서가 줄어들고 있어요, 잠시만 기다려 주세요</p>
        </div>

        {/* 진행 바 */}
        <div className="w-full bg-gray-700 rounded-full h-1.5 mb-6">
          <div
            className="bg-blue-500 h-1.5 rounded-full transition-all duration-700"
            style={{ width: `${Math.max(5, 100 - rank * 10)}%` }}
          />
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
