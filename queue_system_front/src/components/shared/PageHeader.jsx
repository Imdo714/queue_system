export const PageHeader = ({ title, subtitle, actions }) => (
  <header className="max-w-6xl mx-auto flex justify-between items-center mb-12">
    <div>
      <h1 className="text-3xl font-bold">{title}</h1>
      {subtitle && <p className="text-gray-400">{subtitle}</p>}
    </div>
    {actions && <div className="flex gap-4">{actions}</div>}
  </header>
);
