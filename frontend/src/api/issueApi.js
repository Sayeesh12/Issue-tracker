import api from './axios';

export async function fetchIssue(issueId) {
  const { data } = await api.get(`/issues/${issueId}`);
  return data;
}

export async function fetchProjectIssues(projectId, page = 0, size = 50) {
  const { data } = await api.get(`/issues/projects/${projectId}`, {
    params: { page, size },
  });
  return data;
}

export async function fetchMyIssues(page = 0, size = 10) {
  const { data } = await api.get('/issues/my', {
    params: { page, size },
  });
  return data;
}

export async function createIssue(projectId, issue) {
  const { data } = await api.post(`/issues/projects/${projectId}`, issue);
  return data;
}

export async function assignIssue(issueId, assigneeId) {
  const { data } = await api.patch(`/issues/${issueId}/assign`, null, {
    params: { assigneeId },
  });
  return data;
}

export async function updateIssueStatus(issueId, status) {
  const { data } = await api.patch(`/issues/${issueId}/status`, null, {
    params: { status },
  });
  return data;
}

export async function fetchIssueActivities(issueId) {
  const { data } = await api.get(`/issues/${issueId}/activities`);
  return data;
}
