import IssueList from '../components/issue/IssueList';
import { useMyIssues } from '../hooks/useIssues';
import { LoadingState, ErrorState } from '../components/ui/Spinner';

export default function MyIssues() {
  const { issues, loading, error, reload } = useMyIssues();

  return (
    <div className="mx-auto max-w-6xl">
      <div className="mb-8">
        <h1 className="text-2xl font-bold text-slate-900">My Issues</h1>
        <p className="mt-1 text-slate-500">Issues assigned to you across all projects.</p>
      </div>

      {loading ? (
        <LoadingState message="Loading your issues..." />
      ) : error ? (
        <ErrorState message={error} onRetry={reload} />
      ) : (
        <IssueList
          issues={issues}
          emptyTitle="No issues assigned to you"
          emptyDescription="When someone assigns you an issue, it will show up here."
        />
      )}
    </div>
  );
}
