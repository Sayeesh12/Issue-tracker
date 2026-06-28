import { useAuth } from '../../hooks/useAuth';
import Button from '../ui/Button';

export default function Navbar() {
  const { email, logout } = useAuth();

  return (
    <header className="flex h-16 items-center justify-between border-b border-slate-200 bg-white px-4 lg:px-8">
      <div className="lg:hidden">
        <span className="text-lg font-semibold text-slate-900">IssueTracker</span>
      </div>
      <div className="hidden lg:block">
        <p className="text-sm text-slate-500">Track issues, ship faster</p>
      </div>
      <div className="flex items-center gap-4">
        {email && (
          <div className="hidden items-center gap-2 sm:flex">
            <div className="flex h-8 w-8 items-center justify-center rounded-full bg-brand-100 text-sm font-medium text-brand-700">
              {email.charAt(0).toUpperCase()}
            </div>
            <span className="text-sm text-slate-600">{email}</span>
          </div>
        )}
        <Button variant="secondary" size="sm" onClick={logout}>
          Log out
        </Button>
      </div>
    </header>
  );
}
