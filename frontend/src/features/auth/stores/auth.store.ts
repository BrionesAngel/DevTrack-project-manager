import { computed, ref } from 'vue'
import { defineStore } from 'pinia'
import { authService } from '../services/auth.service'
import type { LoginRequest, RegisterRequest } from '../dtos/auth.dtos.ts'

const ACCESS_TOKEN_KEY = 'accessToken'
const REFRESH_TOKEN_KEY = 'refreshToken'

export const useAuthStore = defineStore('auth', () => {
  const accessToken = ref<string | null>(null)
  const refreshToken = ref<string | null>(null)
  const isLoading = ref(false)

  function initializeFromStorage() {
    accessToken.value = localStorage.getItem(ACCESS_TOKEN_KEY)
    refreshToken.value = localStorage.getItem(REFRESH_TOKEN_KEY)
  }
  initializeFromStorage()

  const isAuthenticated = computed(() => Boolean(accessToken.value))

  async function login(credentials: LoginRequest) {
    isLoading.value = true
    try {
      const { data } = await authService.login(credentials)
      accessToken.value = data.accessToken
      refreshToken.value = data.refreshToken
      localStorage.setItem(ACCESS_TOKEN_KEY, data.accessToken)
      localStorage.setItem(REFRESH_TOKEN_KEY, data.refreshToken)
    } finally {
      isLoading.value = false
    }
  }

  async function register(credentials: RegisterRequest) {
    isLoading.value = true
    try {
      const { data } = await authService.register(credentials)
      accessToken.value = data.accessToken
      refreshToken.value = data.refreshToken
      localStorage.setItem(ACCESS_TOKEN_KEY, data.accessToken)
      localStorage.setItem(REFRESH_TOKEN_KEY, data.refreshToken)
    } finally {
      isLoading.value = false
    }
  }

  function logout() {
    accessToken.value = null
    refreshToken.value = null
    localStorage.removeItem(ACCESS_TOKEN_KEY)
    localStorage.removeItem(REFRESH_TOKEN_KEY)
  }

  return {
    accessToken,
    refreshToken,
    isLoading,
    isAuthenticated,
    login,
    register,
    logout,
  }
})
