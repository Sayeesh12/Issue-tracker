import { createContext, useCallback, useMemo, useState } from 'react';
import * as authApi from '../api/authApi';
import {
  getEmail,
  getToken,
  getUserId,
  isAuthenticated,
  removeToken,
  setEmail,
  setToken,
  setUserId,
} from '../utils/token';

export const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => ({
    email: getEmail(),
    userId: getUserId(),
    authenticated: isAuthenticated(),
  }));

  const login = useCallback(async (credentials) => {
    const { token } = await authApi.login(credentials);
    setToken(token);
    setEmail(credentials.email);
    setUser({
      email: credentials.email,
      userId: getUserId(),
      authenticated: true,
    });
    return { token };
  }, []);

  const register = useCallback(async (userData) => {
    const { token } = await authApi.register(userData);
    setToken(token);
    setEmail(userData.email);
    setUser({
      email: userData.email,
      userId: getUserId(),
      authenticated: true,
    });
    return { token };
  }, []);

  const logout = useCallback(() => {
    removeToken();
    setUser({ email: null, userId: null, authenticated: false });
  }, []);

  const updateUserId = useCallback((id) => {
    setUserId(id);
    setUser((prev) => ({ ...prev, userId: id }));
  }, []);

  const value = useMemo(
    () => ({
      ...user,
      login,
      register,
      logout,
      updateUserId,
      isAuthenticated: user.authenticated && Boolean(getToken()),
    }),
    [user, login, register, logout, updateUserId],
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}
