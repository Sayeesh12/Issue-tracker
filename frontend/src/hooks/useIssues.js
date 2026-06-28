import { useCallback, useEffect, useState } from 'react';
import * as issueApi from '../api/issueApi';
import * as commentApi from '../api/commentApi';
import { getErrorMessage } from '../utils/apiError';
import { useAuth } from './useAuth';

export function useIssues(projectId) {
  const [issues, setIssues] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const { updateUserId } = useAuth();

  const loadIssues = useCallback(async () => {
    if (!projectId) {
      setLoading(false);
      return;
    }
    setLoading(true);
    setError(null);
    try {
      const page = await issueApi.fetchProjectIssues(projectId);
      const content = page.content ?? page;
      setIssues(content);
    } catch (err) {
      setError(getErrorMessage(err, 'Failed to load issues'));
    } finally {
      setLoading(false);
    }
  }, [projectId]);

  useEffect(() => {
    loadIssues();
  }, [loadIssues]);

  const createIssue = useCallback(
    async (issueData) => {
      const created = await issueApi.createIssue(projectId, issueData);
      if (created.creatorId) updateUserId(created.creatorId);
      setIssues((prev) => [created, ...prev]);
      return created;
    },
    [projectId, updateUserId],
  );

  const assignIssue = useCallback(async (issueId, assigneeId) => {
    const updated = await issueApi.assignIssue(issueId, assigneeId);
    setIssues((prev) => prev.map((i) => (i.id === issueId ? updated : i)));
    return updated;
  }, []);

  const updateStatus = useCallback(async (issueId, status) => {
    const updated = await issueApi.updateIssueStatus(issueId, status);
    setIssues((prev) => prev.map((i) => (i.id === issueId ? updated : i)));
    return updated;
  }, []);

  return {
    issues,
    loading,
    error,
    reload: loadIssues,
    createIssue,
    assignIssue,
    updateStatus,
  };
}

export function useMyIssues(size = 50) {
  const [issues, setIssues] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const { updateUserId } = useAuth();

  const loadMyIssues = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const page = await issueApi.fetchMyIssues(0, size);
      const content = page.content ?? page;
      setIssues(content);
      if (content.length > 0 && content[0].assigneeId) {
        updateUserId(content[0].assigneeId);
      }
    } catch (err) {
      setError(getErrorMessage(err, 'Failed to load your issues'));
    } finally {
      setLoading(false);
    }
  }, [updateUserId, size]);

  useEffect(() => {
    loadMyIssues();
  }, [loadMyIssues]);

  return { issues, loading, error, reload: loadMyIssues };
}

export function useIssueDetail(issueId) {
  const [issue, setIssue] = useState(null);
  const [activities, setActivities] = useState([]);
  const [comments, setComments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const { updateUserId } = useAuth();

  const loadDetail = useCallback(async () => {
    if (!issueId) {
      setLoading(false);
      return;
    }
    setLoading(true);
    setError(null);
    try {
      const [issueData, activityData, commentData] = await Promise.all([
        issueApi.fetchIssue(issueId),
        issueApi.fetchIssueActivities(issueId),
        commentApi.fetchComments(issueId),
      ]);
      setIssue(issueData);
      setActivities(activityData);
      setComments(commentData);
      if (issueData.creatorId) updateUserId(issueData.creatorId);
    } catch (err) {
      setError(getErrorMessage(err, 'Failed to load issue'));
    } finally {
      setLoading(false);
    }
  }, [issueId, updateUserId]);

  useEffect(() => {
    loadDetail();
  }, [loadDetail]);

  const assignIssue = useCallback(
    async (assigneeId) => {
      const updated = await issueApi.assignIssue(issueId, assigneeId);
      setIssue(updated);
      const activityData = await issueApi.fetchIssueActivities(issueId);
      setActivities(activityData);
      return updated;
    },
    [issueId],
  );

  const updateStatus = useCallback(
    async (status) => {
      const updated = await issueApi.updateIssueStatus(issueId, status);
      setIssue(updated);
      const activityData = await issueApi.fetchIssueActivities(issueId);
      setActivities(activityData);
      return updated;
    },
    [issueId],
  );

  const addComment = useCallback(
    async (content) => {
      const created = await commentApi.addComment(issueId, content);
      setComments((prev) => [...prev, created]);
      return created;
    },
    [issueId],
  );

  return {
    issue,
    activities,
    comments,
    loading,
    error,
    reload: loadDetail,
    assignIssue,
    updateStatus,
    addComment,
  };
}
