import { Link } from 'react-router-dom';
import ProjectCard from '../components/project/ProjectCard';
import IssueList from '../components/issue/IssueList';
import { useProjects } from '../hooks/useProjects';
import { useMyIssues } from '../hooks/useIssues';
import { LoadingState, ErrorState, EmptyState } from '../components/ui/Spinner';
import { useAuth } from '../hooks/useAuth';

export default function Dashboard() {
  const { email } = useAuth();
  const { projects, loading: projectsLoading, error: projectsError, reload: reloadProjects } = useProjects();
  const { issues, loading: issuesLoading, error: issuesError, reload: reloadIssues } = useMyIssues();

  const recentProjects = projects.slice(0, 4);

  return (
    <div className="mx-auto max-w-6xl space-y-8">
      <div>
        <h1 className="text-2xl font-bold text-slate-900">
          Good {getGreeting()}, {email?.split('@')[0]}
        </h1>
        <p className="mt-1 text-slate-500">Here&apos;s what&apos;s happening across your workspace.</p>
      </div>

      <section>
        <div className="mb-4 flex items-center justify-between">
          <h2 className="text-lg font-semibold text-slate-900">Recent Projects</h2>
          <Link to="/projects" className="text-sm font-medium text-brand-600 hover:text-brand-700">
            View all
          </Link>
        </div>
        {projectsLoading ? (
          <LoadingState message="Loading projects..." />
        ) : projectsError ? (
          <ErrorState message={projectsError} onRetry={reloadProjects} />
        ) : recentProjects.length === 0 ? (
          <EmptyState
            title="No projects yet"
            description="Create a project to start tracking issues with your team."
            action={
              <Link
                to="/projects"
                className="inline-flex items-center rounded-lg bg-brand-600 px-4 py-2 text-sm font-medium text-white hover:bg-brand-700"
              >
                Go to Projects
              </Link>
            }
          />
        ) : (
          <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
            {recentProjects.map((project) => (
              <ProjectCard key={project.id} project={project} />
            ))}
          </div>
        )}
      </section>

      <section>
        <div className="mb-4 flex items-center justify-between">
          <h2 className="text-lg font-semibold text-slate-900">My Issues</h2>
        </div>
        {issuesLoading ? (
          <LoadingState message="Loading your issues..." />
        ) : issuesError ? (
          <ErrorState message={issuesError} onRetry={reloadIssues} />
        ) : (
          <IssueList
            issues={issues}
            emptyTitle="No issues assigned to you"
            emptyDescription="Issues assigned to you will appear here."
          />
        )}
      </section>
    </div>
  );
}

function getGreeting() {
  const hour = new Date().getHours();
  if (hour < 12) return 'morning';
  if (hour < 17) return 'afternoon';
  return 'evening';
}
