import { useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { StatusBadge, TypeBadge } from '../components/ui/Badge';
import Button from '../components/ui/Button';
import { useIssueDetail } from '../hooks/useIssues';
import { useProject } from '../hooks/useProjects';
import { LoadingState, ErrorState, EmptyState } from '../components/ui/Spinner';
import { useToast } from '../components/ui/Toast';
import { formatDate, formatRelativeDate } from '../utils/formatDate';
import { getErrorMessage } from '../utils/apiError';
import { formatMemberLabel } from '../utils/member';

const STATUS_FLOW = {
  OPEN: { next: 'IN_PROGRESS', label: 'Start Progress' },
  IN_PROGRESS: { next: 'DONE', label: 'Mark Done' },
};

const ACTIVITY_LABELS = {
  CREATED: 'created this issue',
  ASSIGNED: 'assigned the issue',
  STATUS_CHANGED: 'changed the status',
};

export default function IssueDetail() {
  const { id } = useParams();
  const issueId = Number(id);
  const { showToast } = useToast();

  const {
    issue,
    activities,
    comments,
    loading,
    error,
    reload,
    assignIssue,
    updateStatus,
    addComment,
  } = useIssueDetail(issueId);

  const { project } = useProject(issue?.projectId);
  const [commentText, setCommentText] = useState('');
  const [commentError, setCommentError] = useState('');
  const [submittingComment, setSubmittingComment] = useState(false);
  const [actionLoading, setActionLoading] = useState(false);

  const handleStatusUpdate = async () => {
    const flow = STATUS_FLOW[issue.status];
    if (!flow) return;

    setActionLoading(true);
    try {
      await updateStatus(flow.next);
      showToast(`Status updated to ${flow.next.replace('_', ' ')}`, 'success');
    } catch (err) {
      showToast(getErrorMessage(err, 'Failed to update status'), 'error');
    } finally {
      setActionLoading(false);
    }
  };

  const handleAssign = async (assigneeId) => {
    if (!assigneeId) return;

    setActionLoading(true);
    try {
      await assignIssue(Number(assigneeId));
      showToast('Issue assigned successfully', 'success');
    } catch (err) {
      showToast(getErrorMessage(err, 'Failed to assign issue'), 'error');
    } finally {
      setActionLoading(false);
    }
  };

  const handleAddComment = async (e) => {
    e.preventDefault();
    setCommentError('');

    if (!commentText.trim()) {
      setCommentError('Comment cannot be empty');
      return;
    }

    setSubmittingComment(true);
    try {
      await addComment(commentText.trim());
      setCommentText('');
      showToast('Comment added', 'success');
    } catch (err) {
      showToast(getErrorMessage(err, 'Failed to add comment'), 'error');
    } finally {
      setSubmittingComment(false);
    }
  };

  if (loading) {
    return <LoadingState message="Loading issue..." />;
  }

  if (error || !issue) {
    return <ErrorState message={error || 'Issue not found'} onRetry={reload} />;
  }

  const statusAction = STATUS_FLOW[issue.status];
  const members = project?.members ?? [];

  return (
    <div className="mx-auto max-w-5xl">
      <div className="mb-6">
        <Link
          to={`/projects/${issue.projectId}`}
          className="text-sm text-slate-500 hover:text-brand-600"
        >
          ← Back to project
        </Link>
      </div>

      <div className="grid gap-6 lg:grid-cols-3">
        <div className="space-y-6 lg:col-span-2">
          <div className="rounded-xl border border-slate-200 bg-white p-6 shadow-card">
            <div className="mb-4 flex flex-wrap items-center gap-2">
              <TypeBadge type={issue.type} />
              <StatusBadge status={issue.status} />
              <span className="text-sm text-slate-400">#{issue.id}</span>
            </div>
            <h1 className="text-2xl font-bold text-slate-900">{issue.title}</h1>
            {issue.description ? (
              <p className="mt-4 whitespace-pre-wrap text-slate-600">{issue.description}</p>
            ) : (
              <p className="mt-4 text-sm italic text-slate-400">No description provided.</p>
            )}
          </div>

          <section className="rounded-xl border border-slate-200 bg-white p-6 shadow-card">
            <h2 className="mb-4 text-lg font-semibold text-slate-900">Comments</h2>

            <form onSubmit={handleAddComment} className="mb-6">
              <textarea
                rows={3}
                value={commentText}
                onChange={(e) => setCommentText(e.target.value)}
                placeholder="Write a comment..."
                className={`block w-full rounded-lg border bg-white px-3.5 py-2.5 text-sm shadow-sm focus:border-brand-500 focus:outline-none focus:ring-2 focus:ring-brand-500/20 ${
                  commentError ? 'border-red-300' : 'border-slate-200'
                }`}
              />
              {commentError && <p className="mt-1 text-sm text-red-600">{commentError}</p>}
              <div className="mt-3 flex justify-end">
                <Button type="submit" size="sm" disabled={submittingComment}>
                  {submittingComment ? 'Posting...' : 'Post Comment'}
                </Button>
              </div>
            </form>

            {comments.length === 0 ? (
              <EmptyState
                title="No comments yet"
                description="Be the first to leave a comment on this issue."
              />
            ) : (
              <div className="space-y-4">
                {comments.map((comment) => (
                  <div key={comment.id} className="rounded-lg bg-slate-50 p-4">
                    <div className="flex items-center justify-between">
                      <span className="text-sm font-medium text-slate-900">
                        {comment.authorName || `User #${comment.authorId}`}
                      </span>
                      <span className="text-xs text-slate-400">
                        {formatRelativeDate(comment.createdAt)}
                      </span>
                    </div>
                    <p className="mt-2 text-sm text-slate-600">{comment.content}</p>
                  </div>
                ))}
              </div>
            )}
          </section>
        </div>

        <div className="space-y-6">
          <div className="rounded-xl border border-slate-200 bg-white p-5 shadow-card">
            <h3 className="mb-4 text-sm font-semibold uppercase tracking-wide text-slate-500">
              Details
            </h3>
            <dl className="space-y-3 text-sm">
              <div>
                <dt className="text-slate-500">Status</dt>
                <dd className="mt-1">
                  <StatusBadge status={issue.status} />
                </dd>
              </div>
              <div>
                <dt className="text-slate-500">Assignee</dt>
                <dd className="mt-1 text-slate-900">
                  {issue.assigneeName || (issue.assigneeId ? `Member #${issue.assigneeId}` : 'Unassigned')}
                </dd>
              </div>
              <div>
                <dt className="text-slate-500">Creator</dt>
                <dd className="mt-1 text-slate-900">
                  {issue.creatorName || `Member #${issue.creatorId}`}
                </dd>
              </div>
            </dl>

            <div className="mt-5 space-y-3 border-t border-slate-100 pt-5">
              {statusAction && (
                <Button
                  className="w-full"
                  onClick={handleStatusUpdate}
                  disabled={actionLoading}
                >
                  {actionLoading ? 'Updating...' : statusAction.label}
                </Button>
              )}

              <div>
                <label htmlFor="assignee" className="mb-1.5 block text-xs font-medium text-slate-500">
                  Assign to
                </label>
                <select
                  id="assignee"
                  key={issue.assigneeId ?? 'none'}
                  defaultValue={issue.assigneeId ?? ''}
                  onChange={(e) => handleAssign(e.target.value)}
                  disabled={actionLoading}
                  className="block w-full rounded-lg border border-slate-200 bg-white px-3 py-2 text-sm focus:border-brand-500 focus:outline-none focus:ring-2 focus:ring-brand-500/20"
                >
                  <option value="">Select member</option>
                  {members.map((member) => (
                    <option key={member.id} value={member.id}>
                      {formatMemberLabel(member)}
                    </option>
                  ))}
                </select>
                {members.length <= 1 && (
                  <p className="mt-2 text-xs text-slate-500">
                    Add more team members on the project page to assign issues to others.
                  </p>
                )}
              </div>
            </div>
          </div>

          <div className="rounded-xl border border-slate-200 bg-white p-5 shadow-card">
            <h3 className="mb-4 text-sm font-semibold uppercase tracking-wide text-slate-500">
              Activity
            </h3>
            {activities.length === 0 ? (
              <p className="text-sm text-slate-400">No activity yet.</p>
            ) : (
              <ul className="space-y-4">
                {activities.map((activity, index) => (
                  <li key={index} className="relative pl-4">
                    <span className="absolute left-0 top-1.5 h-2 w-2 rounded-full bg-brand-400" />
                    <p className="text-sm text-slate-700">
                      <span className="font-medium">{activity.performedBy}</span>{' '}
                      {ACTIVITY_LABELS[activity.action] ?? activity.action.toLowerCase()}
                    </p>
                    {activity.details && (
                      <p className="text-xs text-slate-500">{activity.details}</p>
                    )}
                    <p className="mt-0.5 text-xs text-slate-400">
                      {formatDate(activity.createdAt)}
                    </p>
                  </li>
                ))}
              </ul>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}
