import axios from 'axios'

export class HttpError extends Error {
  status: number
  constructor(message: string, status: number) {
    super(message)
    this.name = 'HttpError'
    this.status = status
  }
}

const baseConfig = {
  baseURL: import.meta.env.VITE_API_URL || 'http://localhost:8080',
  headers: {
    'Content-Type': 'application/json'
  }
}

const publicApi = axios.create(baseConfig)
const privateApi = axios.create(baseConfig)

privateApi.interceptors.request.use(config => {
  const accessToken = localStorage.getItem('accessToken')
  if (accessToken) {
    config.headers.Authorization = `Bearer ${accessToken}`
  }
  return config
})

privateApi.interceptors.response.use(
  response => response,
  async error => {
    const originalRequest = error.config

    if ((error.response?.status === 401 || error.response?.status === 403) && !originalRequest._retry) {
      originalRequest._retry = true

      try {
        const refreshToken = localStorage.getItem('refreshToken')

        const response = await publicApi.post(
          '/auth/refresh',
          { refreshToken }
        )

        const { accessToken, refreshToken: newRefreshToken } = response.data

        localStorage.setItem('accessToken', accessToken)
        localStorage.setItem('refreshToken', newRefreshToken)

        originalRequest.headers.Authorization = `Bearer ${accessToken}`
        return privateApi(originalRequest)
      } catch (refreshError) {
        localStorage.removeItem('accessToken')
        localStorage.removeItem('refreshToken')
        window.location.href = '/login'
        return Promise.reject(refreshError)
      }
    }

    return Promise.reject(error)
  }
)

import { getFriendlyMessage } from './error.messages'

const handleError = (error: any) => {
  if (!error.response) {
    return Promise.reject(new HttpError('Network error', 0))
  }
  const status = error.response.status
  const code = error.response.data?.message ?? `Request failed with status ${status}`
  const message = getFriendlyMessage(code)
  return Promise.reject(new HttpError(message, status))
}

publicApi.interceptors.response.use(res => res, handleError)
privateApi.interceptors.response.use(res => res, handleError)

export { publicApi, privateApi }
