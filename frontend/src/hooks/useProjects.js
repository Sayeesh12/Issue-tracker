import { useCallback, useEffect, useState } from 'react';
import * as projectApi from '../api/projectApi';
import { getErrorMessage } from '../utils/apiError';

export function useProjects() {
  const [projects, setProjects] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const loadProjects = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await projectApi.fetchProjects();
      setProjects(data);
    } catch (err) {
      setError(getErrorMessage(err, 'Failed to load projects'));
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    loadProjects();
  }, [loadProjects]);

  const createProject = useCallback(async (projectData) => {
    const created = await projectApi.createProject(projectData);
    setProjects((prev) => [...prev, created]);
    return created;
  }, []);

  return {
    projects,
    loading,
    error,
    reload: loadProjects,
    createProject,
  };
}

export function useProject(projectId) {
  const [project, setProject] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const loadProject = useCallback(async () => {
    if (!projectId) {
      setLoading(false);
      return;
    }
    setLoading(true);
    setError(null);
    try {
      const data = await projectApi.fetchProject(projectId);
      setProject(data);
    } catch (err) {
      setError(getErrorMessage(err, 'Failed to load project'));
    } finally {
      setLoading(false);
    }
  }, [projectId]);

  useEffect(() => {
    loadProject();
  }, [loadProject]);

  return { project, loading, error, reload: loadProject };
}
