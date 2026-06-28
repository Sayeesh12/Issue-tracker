export function getErrorMessage(error, fallback = 'Something went wrong') {
  const data = error?.response?.data;

  if (typeof data === 'string') return data;
  if (data?.message) return data.message;
  if (error?.message) return error.message;

  return fallback;
}
