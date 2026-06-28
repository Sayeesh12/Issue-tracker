import IssueCard from './IssueCard';
import { EmptyState } from '../ui/Spinner';

export default function IssueList({ issues, emptyTitle = 'No issues', emptyDescription }) {
  if (!issues.length) {
    return (
      <EmptyState
        title={emptyTitle}
        description={emptyDescription}
        icon={
          <svg className="h-12 w-12" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1} d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
          </svg>
        }
      />
    );
  }

  return (
    <div className="grid gap-3">
      {issues.map((issue) => (
        <IssueCard key={issue.id} issue={issue} />
      ))}
    </div>
  );
}
