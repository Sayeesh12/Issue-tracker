import { useState } from 'react';
import Modal, { ModalActions } from '../ui/Modal';
import Input from '../ui/Input';
import { getErrorMessage } from '../../utils/apiError';
import { useToast } from '../ui/Toast';

const ISSUE_TYPES = ['BUG', 'FEATURE', 'IMPROVEMENT'];

export default function CreateIssueModal({ isOpen, onClose, onCreate }) {
  const { showToast } = useToast();
  const [form, setForm] = useState({ title: '', description: '', type: 'BUG' });
  const [errors, setErrors] = useState({});
  const [loading, setLoading] = useState(false);

  const resetForm = () => {
    setForm({ title: '', description: '', type: 'BUG' });
    setErrors({});
  };

  const handleClose = () => {
    resetForm();
    onClose();
  };

  const validate = () => {
    const next = {};
    if (!form.title.trim()) next.title = 'Title is required';
    if (!form.type) next.type = 'Type is required';
    setErrors(next);
    return Object.keys(next).length === 0;
  };

  const handleSubmit = async () => {
    if (!validate()) return;

    setLoading(true);
    try {
      await onCreate({
        title: form.title.trim(),
        description: form.description.trim() || undefined,
        type: form.type,
      });
      showToast('Issue created successfully', 'success');
      handleClose();
    } catch (err) {
      showToast(getErrorMessage(err, 'Failed to create issue'), 'error');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Modal
      isOpen={isOpen}
      onClose={handleClose}
      title="Create Issue"
      footer={
        <ModalActions
          onCancel={handleClose}
          onConfirm={handleSubmit}
          confirmLabel="Create Issue"
          loading={loading}
        />
      }
    >
      <div className="space-y-4">
        <Input
          label="Title"
          name="title"
          value={form.title}
          onChange={(e) => setForm({ ...form, title: e.target.value })}
          placeholder="Brief summary of the issue"
          error={errors.title}
        />
        <div className="space-y-1.5">
          <label htmlFor="description" className="block text-sm font-medium text-slate-700">
            Description
          </label>
          <textarea
            id="description"
            rows={4}
            value={form.description}
            onChange={(e) => setForm({ ...form, description: e.target.value })}
            placeholder="Add more details..."
            className="block w-full rounded-lg border border-slate-200 bg-white px-3.5 py-2.5 text-sm text-slate-900 shadow-sm placeholder:text-slate-400 focus:border-brand-500 focus:outline-none focus:ring-2 focus:ring-brand-500/20"
          />
        </div>
        <div className="space-y-1.5">
          <label htmlFor="type" className="block text-sm font-medium text-slate-700">
            Type
          </label>
          <select
            id="type"
            value={form.type}
            onChange={(e) => setForm({ ...form, type: e.target.value })}
            className="block w-full rounded-lg border border-slate-200 bg-white px-3.5 py-2.5 text-sm text-slate-900 shadow-sm focus:border-brand-500 focus:outline-none focus:ring-2 focus:ring-brand-500/20"
          >
            {ISSUE_TYPES.map((type) => (
              <option key={type} value={type}>
                {type}
              </option>
            ))}
          </select>
          {errors.type && <p className="text-sm text-red-600">{errors.type}</p>}
        </div>
      </div>
    </Modal>
  );
}
