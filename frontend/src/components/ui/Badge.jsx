const statusStyles = {
  OPEN: 'bg-slate-100 text-slate-700 ring-slate-200',
  IN_PROGRESS: 'bg-blue-50 text-blue-700 ring-blue-200',
  DONE: 'bg-emerald-50 text-emerald-700 ring-emerald-200',
};

const statusLabels = {
  OPEN: 'Open',
  IN_PROGRESS: 'In Progress',
  DONE: 'Done',
};

const typeStyles = {
  BUG: 'bg-red-50 text-red-700 ring-red-200',
  FEATURE: 'bg-violet-50 text-violet-700 ring-violet-200',
  IMPROVEMENT: 'bg-amber-50 text-amber-700 ring-amber-200',
};

export function StatusBadge({ status }) {
  return (
    <span
      className={`inline-flex items-center rounded-md px-2 py-0.5 text-xs font-medium ring-1 ring-inset ${statusStyles[status] ?? statusStyles.OPEN}`}
    >
      {statusLabels[status] ?? status}
    </span>
  );
}

export function TypeBadge({ type }) {
  return (
    <span
      className={`inline-flex items-center rounded-md px-2 py-0.5 text-xs font-medium ring-1 ring-inset ${typeStyles[type] ?? 'bg-slate-50 text-slate-600 ring-slate-200'}`}
    >
      {type}
    </span>
  );
}

export default StatusBadge;
