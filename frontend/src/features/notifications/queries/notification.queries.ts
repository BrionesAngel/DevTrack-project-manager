import { useMutation, useQuery, useQueryClient } from '@tanstack/vue-query'
import { watch } from 'vue'
import type { NotificationCreateRequest, NotificationResponse } from '../DTOs/notification.dtos'
import { notificationService } from '../services/notification.service'

const handledNotificationIds = new Set<number>()

function shouldInvalidateRelatedQueries(notification: NotificationResponse) {
  return notification.type.endsWith('_ACCEPTED') || notification.type.endsWith('_REJECTED') || notification.status === 'READ'
}

function invalidateRelatedQueries(notification: NotificationResponse, queryClient: ReturnType<typeof useQueryClient>) {
  if (notification.resourceType === 'PROJECT') {
    queryClient.invalidateQueries({ queryKey: ['project', notification.resourceId] })
    queryClient.invalidateQueries({ queryKey: ['projects'] })
    queryClient.invalidateQueries({ queryKey: ['projects', 'teams'] })
    queryClient.invalidateQueries({ queryKey: ['dashboard', 'summary'] })
    queryClient.invalidateQueries({ queryKey: ['tasks'] })
    return
  }

  if (notification.resourceType === 'TEAM') {
    queryClient.invalidateQueries({ queryKey: ['team'] })
    queryClient.invalidateQueries({ queryKey: ['project'] })
    queryClient.invalidateQueries({ queryKey: ['projects'] })
    queryClient.invalidateQueries({ queryKey: ['projects', 'teams'] })
    queryClient.invalidateQueries({ queryKey: ['dashboard', 'summary'] })
    queryClient.invalidateQueries({ queryKey: ['tasks'] })
    return
  }

  if (notification.resourceType === 'TASK') {
    queryClient.invalidateQueries({ queryKey: ['tasks'] })
    queryClient.invalidateQueries({ queryKey: ['notifications'] })
    queryClient.invalidateQueries({ queryKey: ['dashboard', 'summary'] })
    queryClient.invalidateQueries({ queryKey: ['project'] })
  }
}

export function useMyNotificationsQuery() {
  const queryClient = useQueryClient()

  const query = useQuery({
    queryKey: ['notifications'],
    queryFn: () => notificationService.getMyNotifications(),
    staleTime: 1000 * 60 * 2,
    refetchInterval: 15000,
    refetchIntervalInBackground: true,
  })

  watch(
    () => query.data.value,
    (notifications) => {
      notifications?.forEach((notification) => {
        if (!handledNotificationIds.has(notification.id) && shouldInvalidateRelatedQueries(notification)) {
          handledNotificationIds.add(notification.id)
          invalidateRelatedQueries(notification, queryClient)
        }
      })
    },
    { immediate: true }
  )

  return query
}

export function useCreateNotificationMutation() {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: (payload: NotificationCreateRequest) => notificationService.createNotification(payload),
    onSuccess: async () => {
      await queryClient.invalidateQueries({ queryKey: ['notifications'] })
    },
  })
}

export function useAcceptNotificationMutation() {
  return useMutation({
    mutationFn: (notificationId: number) => notificationService.acceptNotification(notificationId),
  })
}

export function useRejectNotificationMutation() {
  return useMutation({
    mutationFn: (notificationId: number) => notificationService.rejectNotification(notificationId),
  })
}

export function useMarkNotificationAsReadMutation() {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: (notificationId: number) => notificationService.markNotificationAsRead(notificationId),
    onSuccess: async (notification) => {
      await queryClient.invalidateQueries({ queryKey: ['notifications'] })
      invalidateRelatedQueries(notification, queryClient)
    },
  })
}