import { useState } from 'react';
import ProjectCard from '../components/project/ProjectCard';
import Button from '../components/ui/Button';
import Input from '../components/ui/Input';
import Modal, { ModalActions } from '../components/ui/Modal';
import { useProjects } from '../hooks/useProjects';
import { LoadingState, ErrorState, EmptyState } from '../components/ui/Spinner';
import { useToast } from '../components/ui/Toast';
import { getErrorMessage } from '../utils/apiError';

export default function Projects() {
  const { projects, loading, error, reload, createProject } = useProjects();
  const { showToast } = useToast();
  const [modalOpen, setModalOpen] = useState(false);
  const [form, setForm] = useState({ name: '', description: '' });
  const [errors, setErrors] = useState({});
  const [submitting, setSubmitting] = useState(false);

  const validate = () => {
    const next = {};
    if (!form.name.trim()) next.name = 'Project name is required';
    setErrors(next);
    return Object.keys(next).length === 0;
  };

  const handleCreate = async () => {
    if (!validate()) return;

    setSubmitting(true);
    try {
      await createProject({
        name: form.name.trim(),
        description: form.description.trim() || undefined,
      });
      showToast('Project created successfully', 'success');
      setModalOpen(false);
      setForm({ name: '', description: '' });
      setErrors({});
    } catch (err) {
      showToast(getErrorMessage(err, 'Failed to create project'), 'error');
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="mx-auto max-w-6xl">
      <div className="mb-8 flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
        <div>
          <h1 className="text-2xl font-bold text-slate-900">Projects</h1>
          <p className="mt-1 text-slate-500">Manage and organize your team&apos;s work.</p>
        </div>
        <Button onClick={() => setModalOpen(true)}>New Project</Button>
      </div>

      {loading ? (
        <LoadingState message="Loading projects..." />
      ) : error ? (
        <ErrorState message={error} onRetry={reload} />
      ) : projects.length === 0 ? (
        <EmptyState
          title="No projects yet"
          description="Create your first project to start tracking issues."
          action={<Button onClick={() => setModalOpen(true)}>Create Project</Button>}
        />
      ) : (
        <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
          {projects.map((project) => (
            <ProjectCard key={project.id} project={project} />
          ))}
        </div>
      )}

      <Modal
        isOpen={modalOpen}
        onClose={() => setModalOpen(false)}
        title="Create Project"
        footer={
          <ModalActions
            onCancel={() => setModalOpen(false)}
            onConfirm={handleCreate}
            confirmLabel="Create Project"
            loading={submitting}
          />
        }
      >
        <div className="space-y-4">
          <Input
            label="Project name"
            name="name"
            value={form.name}
            onChange={(e) => setForm({ ...form, name: e.target.value })}
            placeholder="e.g. Mobile App"
            error={errors.name}
          />
          <div className="space-y-1.5">
            <label htmlFor="description" className="block text-sm font-medium text-slate-700">
              Description
            </label>
            <textarea
              id="description"
              rows={3}
              value={form.description}
              onChange={(e) => setForm({ ...form, description: e.target.value })}
              placeholder="What is this project about?"
              className="block w-full rounded-lg border border-slate-200 bg-white px-3.5 py-2.5 text-sm shadow-sm focus:border-brand-500 focus:outline-none focus:ring-2 focus:ring-brand-500/20"
            />
          </div>
        </div>
      </Modal>
    </div>
  );
}
