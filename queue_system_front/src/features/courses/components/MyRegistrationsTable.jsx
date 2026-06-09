import { Button } from '../../../components/shared/Button';

const DAY_KO = {
  MONDAY: '월', TUESDAY: '화', WEDNESDAY: '수',
  THURSDAY: '목', FRIDAY: '금', SATURDAY: '토', SUNDAY: '일',
};

const TABLE_HEADERS = ['코드', '강의명', '시간', '신청일', '취소'];

const MyRegistrationRow = ({ reg, onCancel }) => (
  <tr className="hover:bg-gray-800/50 transition-colors">
    <td className="px-4 py-4 text-gray-400 font-mono text-sm">{reg.courseCode}</td>
    <td className="px-4 py-4 text-white font-medium">{reg.title}</td>
    <td className="px-4 py-4 text-gray-300 text-sm">
      {DAY_KO[reg.dayOfWeek] ?? reg.dayOfWeek}{' '}
      {String(reg.startTime).substring(0, 5)} - {String(reg.endTime).substring(0, 5)}
    </td>
    <td className="px-4 py-4 text-gray-400 text-sm">
      {reg.registrationDate
        ? new Date(reg.registrationDate).toLocaleDateString('ko-KR')
        : '-'}
    </td>
    <td className="px-4 py-4 text-right">
      <Button size="sm" variant="danger" onClick={() => onCancel(reg.courseId)}>
        신청 취소
      </Button>
    </td>
  </tr>
);

export const MyRegistrationsTable = ({ registrations, onCancel }) => (
  <table className="w-full text-left border-collapse">
    <thead>
      <tr className="border-b border-gray-700 bg-gray-800">
        {TABLE_HEADERS.map((header, i) => (
          <th
            key={header}
            className={`px-4 py-3 text-sm font-semibold text-gray-300 ${i === TABLE_HEADERS.length - 1 ? 'text-right' : ''}`}
          >
            {header}
          </th>
        ))}
      </tr>
    </thead>
    <tbody className="divide-y divide-gray-700">
      {registrations.map((reg) => (
        <MyRegistrationRow key={reg.id} reg={reg} onCancel={onCancel} />
      ))}
    </tbody>
  </table>
);
