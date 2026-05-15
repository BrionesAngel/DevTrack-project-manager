const errorMessages: Record<string, string> = {
  // Auth
  INVALID_CREDENTIALS: 'Invalid email or password',
  DUPLICATE_EMAIL: 'Email already in use',
  ACCESS_DENIED: 'Unauthorized',

  // Users
  USERNAME_ALREADY_EXISTS: 'Username already in use',
  INVALID_CURRENT_PASSWORD: 'Current password is incorrect',
  PASSWORD_MISMATCH: 'New passwords do not match',

  // General
  USER_NOT_FOUND: 'User not found',
  RESOURCE_NOT_FOUND: 'Resource not found',
  VALIDATION_ERROR: 'Check your input and try again',
  INTERNAL_SERVER_ERROR: 'Something went wrong',
}

export function getFriendlyMessage(code: string): string {
  return errorMessages[code] ?? 'An unexpected error occurred'
}
