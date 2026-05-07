import { ref } from 'vue'

export function useRegisterValidation() {
  const error = ref('')

  function validate(form: {
    username: string
    email: string
    password: string
    confirmPassword: string
  }) {
    error.value = ''

    if (!form.username || !form.email || !form.password || !form.confirmPassword) {
      error.value = 'All fields are required'
      return false
    }

    if (form.username.length < 2) {
      error.value = 'Username must be at least 2 characters'
      return false
    }

    if (form.username.length > 20) {
      error.value = 'Username can be max 20 characters'
      return false
    }

    const emailRe = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
    if (!emailRe.test(form.email)) {
      error.value = 'Enter a valid email'
      return false
    }

    const passwordRe = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&]).{6,}$/
    if (!passwordRe.test(form.password)) {
      error.value = 'Password must have min 6 chars, 1 mayus, 1 minus, 1 number, 1 special char'
      return false
    }

    if (form.password !== form.confirmPassword) {
      error.value = 'Passwords do not match'
      return false
    }

    return true
  }

  return {
    error,
    validate
  }
}
