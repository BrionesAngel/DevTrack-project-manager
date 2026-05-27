import { privateApi } from '@/api/axios'
import type { NotificationCreateRequest, NotificationResponse } from '../DTOs/notification.dtos'

export const notificationService = {
  async getMyNotifications() {
    const { data } = await privateApi.get<NotificationResponse[]>('/notifications')
    return data
  },

  async createNotification(payload: NotificationCreateRequest) {
    const { data } = await privateApi.post<NotificationResponse>('/notifications', payload)
    return data
  },

  async acceptNotification(notificationId: number) {
    const { data } = await privateApi.patch<NotificationResponse>(`/notifications/${notificationId}/accept`)
    return data
  },

  async rejectNotification(notificationId: number) {
    const { data } = await privateApi.patch<NotificationResponse>(`/notifications/${notificationId}/reject`)
    return data
  },

  async markNotificationAsRead(notificationId: number) {
    const { data } = await privateApi.patch<NotificationResponse>(`/notifications/${notificationId}/read`)
    return data
  },
}