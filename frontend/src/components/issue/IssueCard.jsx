import { Link } from 'react-router-dom';
import { StatusBadge, TypeBadge } from '../ui/Badge';

export default function IssueCard({ issue }) {
  return (
    <Link
      to={`/issues/${issue.id}`}
      className="group block rounded-xl border border-slate-200 bg-white p-4 shadow-card transition-all hover:border-brand-200 hover:shadow-card-hover"
    >
      <div className="flex items-start justify-between gap-3">
        <div className="min-w-0 flex-1">
          <div className="mb-2 flex flex-wrap items-center gap-2">
            <TypeBadge type={issue.type} />
            <StatusBadge status={issue.status} />
          </div>
          <h3 className="truncate font-medium text-slate-900 group-hover:text-brand-700">
            {issue.title}
          </h3>
          {issue.description && (
            <p className="mt-1 line-clamp-2 text-sm text-slate-500">{issue.description}</p>
          )}
        </div>
        <span className="flex-shrink-0 text-xs text-slate-400">#{issue.id}</span>
      </div>
      {(issue.assigneeName || issue.assigneeId) && (
        <p className="mt-3 text-xs text-slate-500">
          Assigned to {issue.assigneeName || `Member #${issue.assigneeId}`}
        </p>
      )}
    </Link>
  );
}
