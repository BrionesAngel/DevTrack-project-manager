import { privateApi, publicApi } from '@/api/axios'
import type { LoginRequest, LoginResponse, LogoutRequest, RefreshRequest, RefreshResponse, RegisterRequest, RegisterResponse } from '../dtos/auth.dtos.ts'

export const authService = {
  async login(payload: LoginRequest) {
    const { data } = await publicApi.post<LoginResponse>('/auth/login', payload)
    return data
  },
  async register(payload: RegisterRequest) {
    const { data } = await publicApi.post<RegisterResponse>('/auth/register', payload)
    return data
  },
  async refresh(payload: RefreshRequest) {
    const { data } = await publicApi.post<RefreshResponse>('/auth/refresh', payload)
    return data
  },
  async logout(payload: LogoutRequest) {
    const { data } = await privateApi.post<void>('/auth/logout', payload)
    return data
  }
}
