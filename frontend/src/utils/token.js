const TOKEN_KEY = 'token';
const EMAIL_KEY = 'email';
const USER_ID_KEY = 'userId';

export function getToken() {
  return localStorage.getItem(TOKEN_KEY);
}

export function setToken(token) {
  localStorage.setItem(TOKEN_KEY, token);
}

export function removeToken() {
  localStorage.removeItem(TOKEN_KEY);
  localStorage.removeItem(EMAIL_KEY);
  localStorage.removeItem(USER_ID_KEY);
}

export function getEmail() {
  return localStorage.getItem(EMAIL_KEY);
}

export function setEmail(email) {
  localStorage.setItem(EMAIL_KEY, email);
}

export function getUserId() {
  const id = localStorage.getItem(USER_ID_KEY);
  return id ? Number(id) : null;
}

export function setUserId(id) {
  if (id != null) {
    localStorage.setItem(USER_ID_KEY, String(id));
  }
}

export function isAuthenticated() {
  return Boolean(getToken());
}
