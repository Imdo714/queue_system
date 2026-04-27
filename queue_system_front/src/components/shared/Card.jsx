
/**
 * @param {Object} props
 * @param {React.ReactNode} props.children
 * @param {string} [props.title]
 * @param {string} [props.className]
 */
export const Card = ({ children, title, className = '' }) => {
  return (
    <div className={`bg-gray-800 rounded-lg shadow-lg overflow-hidden border border-gray-700 ${className}`}>
      {title && (
        <div className="px-6 py-4 border-b border-gray-700">
          <h3 className="text-lg font-bold text-white">{title}</h3>
        </div>
      )}
      <div className="px-6 py-4">
        {children}
      </div>
    </div>
  );
};
