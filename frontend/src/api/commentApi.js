import api from './axios';

export async function fetchComments(issueId) {
  const { data } = await api.get(`/comments/issues/${issueId}`);
  return data;
}

export async function addComment(issueId, content) {
  const { data } = await api.post(`/comments/issues/${issueId}`, { content });
  return data;
}

export async function deleteComment(commentId) {
  await api.delete(`/comments/${commentId}`);
}
