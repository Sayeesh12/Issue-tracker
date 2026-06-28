import { Link } from 'react-router-dom';

export default function ProjectCard({ project }) {
  const memberCount = project.memberIds?.length ?? 0;

  return (
    <Link
      to={`/projects/${project.id}`}
      className="group block rounded-xl border border-slate-200 bg-white p-5 shadow-card transition-all hover:border-brand-200 hover:shadow-card-hover"
    >
      <div className="flex items-start justify-between">
        <div className="flex h-10 w-10 items-center justify-center rounded-lg bg-brand-50 text-brand-600 transition-colors group-hover:bg-brand-100">
          <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M3 7v10a2 2 0 002 2h14a2 2 0 002-2V9a2 2 0 00-2-2h-6l-2-2H5a2 2 0 00-2 2z" />
          </svg>
        </div>
        <span className="text-xs font-medium text-slate-400">
          {memberCount} {memberCount === 1 ? 'member' : 'members'}
        </span>
      </div>
      <h3 className="mt-4 font-semibold text-slate-900 group-hover:text-brand-700">
        {project.name}
      </h3>
      {project.description && (
        <p className="mt-1 line-clamp-2 text-sm text-slate-500">{project.description}</p>
      )}
    </Link>
  );
}
