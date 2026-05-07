const errorMessages: Record<string, string> = {
  // Auth
  INVALID_CREDENTIALS: 'Invalid email or password',
  DUPLICATE_EMAIL: 'Email already in use',
  ACCESS_DENIED: 'Unauthorized',

  // General
  USER_NOT_FOUND: 'User not found',
  RESOURCE_NOT_FOUND: 'Resource not found',
  VALIDATION_ERROR: 'Check your input and try again',
  INTERNAL_SERVER_ERROR: 'Something went wrong',
}

export function getFriendlyMessage(code: string): string {
  return errorMessages[code] ?? 'An unexpected error occurred'
}
