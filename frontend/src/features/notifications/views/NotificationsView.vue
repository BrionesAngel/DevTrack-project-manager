<template>
  <div class="mx-auto flex w-full max-w-6xl flex-col gap-6 p-4">
    <div class="flex items-center gap-3">
      <h1 class="text-4xl font-black text-slate-800">Notifications</h1>
    </div>

    <div v-if="isLoading" class="rounded-md border border-slate-200 bg-white p-6 text-slate-500">
      Loading notifications...
    </div>

    <div v-else-if="isError" class="rounded-md border border-rose-200 bg-rose-50 p-6 text-rose-700">
      Failed to load notifications.
      {{ error?.message }}
    </div>

    <div v-else-if="!data?.length" class="rounded-md border border-dashed border-slate-300 bg-white p-10 text-center text-slate-400">
      You do not have notifications yet.
    </div>

    <ul v-else class="grid grid-cols-1 gap-4 lg:grid-cols-2">
      <li v-for="notification in data" :key="notification.id" class="rounded-md border border-slate-200 bg-white p-4 shadow-sm">
        <div class="flex items-start justify-between gap-4">
          <div>
            <p class="text-lg font-bold text-slate-800">{{ labelFor(notification.type) }}</p>
            <p class="text-sm text-slate-500">From {{ notification.senderUsername }}</p>
            <p class="text-sm text-slate-500">{{ resourceLabel(notification.resourceType) }} #{{ notification.resourceId }}</p>
            <p class="mt-2 text-xs uppercase tracking-wide text-slate-400">{{ formatDate(notification.createdAt) }}</p>
          </div>

          <span class="rounded-full px-3 py-1 text-xs font-semibold" :class="statusClass(notification.status)">
            {{ notification.status }}
          </span>
        </div>

        <div v-if="notification.status === 'PENDING' && !isTaskNotification(notification.type)" class="mt-4 flex gap-2">
          <button
            type="button"
            class="rounded-md border-2 border-emerald-700 bg-emerald-100 px-4 py-2 font-medium text-emerald-900 transition hover:bg-emerald-700 hover:text-white disabled:opacity-60"
            :disabled="acceptMutation.isPending.value || rejectMutation.isPending.value"
            @click="accept(notification.id)">
            Accept
          </button>
          <button
            type="button"
            class="rounded-md border-2 border-rose-700 bg-rose-100 px-4 py-2 font-medium text-rose-900 transition hover:bg-rose-700 hover:text-white disabled:opacity-60"
            :disabled="acceptMutation.isPending.value || rejectMutation.isPending.value"
            @click="reject(notification.id)">
            Reject
          </button>
        </div>

        <div v-else-if="notification.status === 'PENDING'" class="mt-4 flex gap-2">
          <button
            type="button"
            class="rounded-md border-2 border-slate-700 bg-slate-100 px-4 py-2 font-medium text-slate-900 transition hover:bg-slate-700 hover:text-white disabled:opacity-60"
            :disabled="readMutation.isPending.value"
            @click="markAsRead(notification.id)">
            Mark as read
          </button>
        </div>
      </li>
    </ul>
  </div>
</template>

<script setup lang="ts">
import { useQueryClient } from '@tanstack/vue-query'
import {
  useAcceptNotificationMutation,
  useMarkNotificationAsReadMutation,
  useMyNotificationsQuery,
  useRejectNotificationMutation,
} from '../queries/notification.queries'
import type { NotificationResourceType, NotificationStatus, NotificationType } from '../DTOs/notification.dtos'
import { toast } from 'vue-sonner'

const { data, isLoading, isError, error } = useMyNotificationsQuery()
const acceptMutation = useAcceptNotificationMutation()
const rejectMutation = useRejectNotificationMutation()
const readMutation = useMarkNotificationAsReadMutation()
const queryClient = useQueryClient()

function isTaskNotification(type: NotificationType) {
  return type === 'TASK_ASSIGNED' || type === 'TASK_STATUS_CHANGED'
}

function labelFor(type: NotificationType) {
  switch (type) {
    case 'PROJECT_INVITATION':
      return 'Project invitation'
    case 'PROJECT_INVITATION_ACCEPTED':
      return 'Project invitation accepted'
    case 'PROJECT_INVITATION_REJECTED':
      return 'Project invitation rejected'
    case 'TEAM_INVITATION':
      return 'Team invitation'
    case 'TEAM_INVITATION_ACCEPTED':
      return 'Team invitation accepted'
    case 'TEAM_INVITATION_REJECTED':
      return 'Team invitation rejected'
    case 'TASK_ASSIGNED':
      return 'Task assigned'
    case 'TASK_STATUS_CHANGED':
      return 'Task status changed'
  }
}

function resourceLabel(type: NotificationResourceType) {
  switch (type) {
    case 'PROJECT':
      return 'Project'
    case 'TEAM':
      return 'Team'
    case 'TASK':
      return 'Task'
  }
}

function formatDate(value: string) {
  return new Date(value).toLocaleString()
}

function statusClass(status: NotificationStatus) {
  switch (status) {
    case 'PENDING':
      return 'bg-amber-100 text-amber-800'
    case 'ACCEPTED':
      return 'bg-emerald-100 text-emerald-800'
    case 'REJECTED':
      return 'bg-rose-100 text-rose-800'
    case 'READ':
      return 'bg-slate-100 text-slate-700'
  }
}

function accept(notificationId: number) {
  acceptMutation.mutateAsync(notificationId)
    .then(async () => {
      await queryClient.refetchQueries({ queryKey: ['notifications'] })
      toast.success('Notification accepted')
    })
    .catch((mutationError) => toast.error(mutationError instanceof Error ? mutationError.message : 'Failed to accept notification'))
}

function reject(notificationId: number) {
  rejectMutation.mutateAsync(notificationId)
    .then(async () => {
      await queryClient.refetchQueries({ queryKey: ['notifications'] })
      toast.success('Notification rejected')
    })
    .catch((mutationError) => toast.error(mutationError instanceof Error ? mutationError.message : 'Failed to reject notification'))
}

function markAsRead(notificationId: number) {
  readMutation.mutateAsync(notificationId)
    .then(async () => {
      await queryClient.refetchQueries({ queryKey: ['notifications'] })
      toast.success('Notification marked as read')
    })
    .catch((mutationError) => toast.error(mutationError instanceof Error ? mutationError.message : 'Failed to mark notification as read'))
}
</script>