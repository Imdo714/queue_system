
/**
 * @param {Object} props
 * @param {string} props.label
 * @param {string} [props.error]
 * @param {React.InputHTMLAttributes<HTMLInputElement>} props.rest
 */
export const Input = ({ label, error, className = '', ...props }) => {
  return (
    <div className="w-full space-y-1">
      {label && (
        <label className="block text-sm font-medium text-gray-300">
          {label}
        </label>
      )}
      <input
        className={`
          w-full px-3 py-2 bg-gray-800 border rounded-md shadow-sm 
          focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent
          ${error ? 'border-red-500' : 'border-gray-700'}
          ${className}
        `}
        {...props}
      />
      {error && <p className="text-sm text-red-500">{error}</p>}
    </div>
  );
};
