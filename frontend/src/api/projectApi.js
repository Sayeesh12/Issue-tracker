import api from './axios';

export async function fetchProjects() {
  const { data } = await api.get('/projects');
  return data;
}

export async function fetchProject(id) {
  const { data } = await api.get(`/projects/${id}`);
  return data;
}

export async function createProject(project) {
  const { data } = await api.post('/projects', project);
  return data;
}

export async function updateProject(id, project) {
  const { data } = await api.put(`/projects/${id}`, project);
  return data;
}

export async function deleteProject(id) {
  await api.delete(`/projects/${id}`);
}

export async function addProjectMember(projectId, email) {
  const { data } = await api.post(`/projects/${projectId}/members`, { email });
  return data;
}
