import { useMemo, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import Button from '../components/ui/Button';
import Input from '../components/ui/Input';
import IssueList from '../components/issue/IssueList';
import CreateIssueModal from '../components/issue/CreateIssueModal';
import { useProject } from '../hooks/useProjects';
import { useIssues } from '../hooks/useIssues';
import { LoadingState, ErrorState } from '../components/ui/Spinner';
import { useAuth } from '../hooks/useAuth';
import { useToast } from '../components/ui/Toast';
import { formatMemberLabel } from '../utils/member';
import { getErrorMessage } from '../utils/apiError';
import * as projectApi from '../api/projectApi';

const STATUS_OPTIONS = [
  { value: '', label: 'All statuses' },
  { value: 'OPEN', label: 'Open' },
  { value: 'IN_PROGRESS', label: 'In Progress' },
  { value: 'DONE', label: 'Done' },
];

export default function ProjectDetail() {
  const { id } = useParams();
  const projectId = Number(id);
  const { userId } = useAuth();
  const { showToast } = useToast();
  const { project, loading: projectLoading, error: projectError, reload: reloadProject } = useProject(projectId);
  const { issues, loading: issuesLoading, error: issuesError, reload: reloadIssues, createIssue } = useIssues(projectId);

  const [modalOpen, setModalOpen] = useState(false);
  const [statusFilter, setStatusFilter] = useState('');
  const [assigneeFilter, setAssigneeFilter] = useState('');
  const [memberEmail, setMemberEmail] = useState('');
  const [addingMember, setAddingMember] = useState(false);

  const members = project?.members ?? [];

  const filteredIssues = useMemo(() => {
    return issues.filter((issue) => {
      if (statusFilter && issue.status !== statusFilter) return false;
      if (assigneeFilter === 'unassigned' && issue.assigneeId) return false;
      if (assigneeFilter === 'me' && issue.assigneeId !== userId) return false;
      if (assigneeFilter && assigneeFilter !== 'unassigned' && assigneeFilter !== 'me') {
        if (issue.assigneeId !== Number(assigneeFilter)) return false;
      }
      return true;
    });
  }, [issues, statusFilter, assigneeFilter, userId]);

  const handleAddMember = async (e) => {
    e.preventDefault();
    if (!memberEmail.trim()) return;

    setAddingMember(true);
    try {
      await projectApi.addProjectMember(projectId, memberEmail.trim());
      showToast('Team member added', 'success');
      setMemberEmail('');
      reloadProject();
    } catch (err) {
      showToast(getErrorMessage(err, 'Failed to add member'), 'error');
    } finally {
      setAddingMember(false);
    }
  };

  if (projectLoading) {
    return <LoadingState message="Loading project..." />;
  }

  if (projectError) {
    return <ErrorState message={projectError} onRetry={reloadProject} />;
  }

  return (
    <div className="mx-auto max-w-6xl">
      <div className="mb-2">
        <Link to="/projects" className="text-sm text-slate-500 hover:text-brand-600">
          ← Back to Projects
        </Link>
      </div>

      <div className="mb-8 flex flex-col gap-4 sm:flex-row sm:items-start sm:justify-between">
        <div>
          <h1 className="text-2xl font-bold text-slate-900">{project.name}</h1>
          {project.description && (
            <p className="mt-1 text-slate-500">{project.description}</p>
          )}
          <p className="mt-2 text-sm text-slate-400">
            {members.length} team {members.length === 1 ? 'member' : 'members'}
          </p>
        </div>
        <Button onClick={() => setModalOpen(true)}>New Issue</Button>
      </div>

      <div className="mb-6 rounded-xl border border-slate-200 bg-white p-4 shadow-card">
        <h3 className="text-sm font-semibold text-slate-900">Team members</h3>
        {members.length > 0 ? (
          <ul className="mt-2 flex flex-wrap gap-2">
            {members.map((member) => (
              <li
                key={member.id}
                className="rounded-full bg-slate-100 px-3 py-1 text-xs font-medium text-slate-700"
              >
                {formatMemberLabel(member)}
              </li>
            ))}
          </ul>
        ) : (
          <p className="mt-2 text-sm text-slate-500">No members yet.</p>
        )}
        <form onSubmit={handleAddMember} className="mt-4 flex flex-col gap-3 sm:flex-row sm:items-end">
          <div className="flex-1">
            <Input
              label="Add member by email"
              name="memberEmail"
              type="email"
              value={memberEmail}
              onChange={(e) => setMemberEmail(e.target.value)}
              placeholder="teammate@company.com"
            />
          </div>
          <Button type="submit" disabled={addingMember || !memberEmail.trim()}>
            {addingMember ? 'Adding...' : 'Add member'}
          </Button>
        </form>
      </div>

      <div className="mb-6 flex flex-wrap gap-3">
        <select
          value={statusFilter}
          onChange={(e) => setStatusFilter(e.target.value)}
          className="rounded-lg border border-slate-200 bg-white px-3 py-2 text-sm shadow-sm focus:border-brand-500 focus:outline-none focus:ring-2 focus:ring-brand-500/20"
        >
          {STATUS_OPTIONS.map((opt) => (
            <option key={opt.value} value={opt.value}>
              {opt.label}
            </option>
          ))}
        </select>

        <select
          value={assigneeFilter}
          onChange={(e) => setAssigneeFilter(e.target.value)}
          className="rounded-lg border border-slate-200 bg-white px-3 py-2 text-sm shadow-sm focus:border-brand-500 focus:outline-none focus:ring-2 focus:ring-brand-500/20"
        >
          <option value="">All assignees</option>
          <option value="unassigned">Unassigned</option>
          {userId && <option value="me">Assigned to me</option>}
          {members.map((member) => (
            <option key={member.id} value={member.id}>
              {formatMemberLabel(member)}
            </option>
          ))}
        </select>
      </div>

      {issuesLoading ? (
        <LoadingState message="Loading issues..." />
      ) : issuesError ? (
        <ErrorState message={issuesError} onRetry={reloadIssues} />
      ) : (
        <IssueList
          issues={filteredIssues}
          emptyTitle="No issues found"
          emptyDescription={
            statusFilter || assigneeFilter
              ? 'Try adjusting your filters.'
              : 'Create your first issue to get started.'
          }
        />
      )}

      <CreateIssueModal
        isOpen={modalOpen}
        onClose={() => setModalOpen(false)}
        onCreate={createIssue}
      />
    </div>
  );
}
