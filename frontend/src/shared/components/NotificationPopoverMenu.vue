<template>
  <div ref="target" class="relative">
    <button
      type="button"
      class="relative flex items-center justify-center rounded-4xl bg-slate-100 p-2 transition hover:bg-slate-200"
      @click="toggleMenu">
      <BellIcon class="size-8 text-slate-700" />
      <span
        v-if="pendingCount > 0"
        class="absolute -right-1 -top-1 flex min-h-5 min-w-5 items-center justify-center rounded-full bg-rose-600 px-1 text-xs font-bold text-white">
        {{ pendingCount }}
      </span>
    </button>

    <div
      v-show="isOpen"
      ref="popover"
      class="absolute right-0 top-full z-50 mt-4 w-88 rounded-xl border border-gray-200 bg-white p-4 shadow-lg">
      <div class="flex items-center justify-between gap-3 border-b border-slate-100 pb-3">
        <div>
          <h3 class="text-lg font-bold text-slate-800">Notifications</h3>
          <p class="text-sm text-slate-500">Latest invitations and updates</p>
        </div>
        <RouterLink
          :to="{ name: 'notifications' }"
          class="text-sm font-medium text-blue-700 hover:text-blue-900"
          @click="closeMenu">
          View all
        </RouterLink>
      </div>

      <div v-if="isLoading" class="py-6 text-sm text-slate-500">
        Loading notifications...
      </div>

      <div v-else-if="isError" class="py-6 text-sm text-rose-600">
        {{ error?.message }}
      </div>

      <div v-else-if="!pendingNotifications.length" class="py-6 text-sm text-slate-500">
        You have no pending notifications.
      </div>

      <div v-else class="mt-3 flex max-h-104 flex-col gap-3 overflow-y-auto pr-1">
        <article
          v-for="notification in pendingNotifications"
          :key="notification.id"
          class="rounded-lg border border-slate-200 bg-slate-50 p-3">
          <div class="flex items-start justify-between gap-3">
            <div class="min-w-0">
              <p class="truncate text-sm font-semibold text-slate-800">
                {{ notificationLabel(notification.type) }}
              </p>
              <p class="text-xs text-slate-500">
                From {{ notification.senderUsername }} · {{ resourceLabel(notification.resourceType) }} #{{ notification.resourceId }}
              </p>
            </div>
            <span class="rounded-full bg-amber-100 px-2 py-1 text-[11px] font-semibold uppercase tracking-wide text-amber-800">
              {{ notification.status }}
            </span>
          </div>

          <div class="mt-3 flex gap-2" v-if="!isTaskNotification(notification.type)">
            <button
              type="button"
              class="rounded-md border-2 border-emerald-700 bg-emerald-100 px-3 py-1.5 text-sm font-medium text-emerald-900 transition hover:bg-emerald-700 hover:text-white disabled:opacity-60"
              :disabled="isProcessing"
              @click="accept(notification.id)">
              Accept
            </button>
            <button
              type="button"
              class="rounded-md border-2 border-rose-700 bg-rose-100 px-3 py-1.5 text-sm font-medium text-rose-900 transition hover:bg-rose-700 hover:text-white disabled:opacity-60"
              :disabled="isProcessing"
              @click="reject(notification.id)">
              Reject
            </button>
          </div>

          <div class="mt-3 flex gap-2" v-else>
            <button
              type="button"
              class="rounded-md border-2 border-slate-700 bg-slate-100 px-3 py-1.5 text-sm font-medium text-slate-900 transition hover:bg-slate-700 hover:text-white disabled:opacity-60"
              :disabled="isProcessing"
              @click="markAsRead(notification.id)">
              Mark as read
            </button>
          </div>
        </article>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { onClickOutside } from '@vueuse/core'
import { BellIcon } from '@lucide/vue'
import { toast } from 'vue-sonner'
import { RouterLink } from 'vue-router'
import { useQueryClient } from '@tanstack/vue-query'
import {
  useAcceptNotificationMutation,
  useMarkNotificationAsReadMutation,
  useMyNotificationsQuery,
  useRejectNotificationMutation,
} from '@/features/notifications/queries/notification.queries'
import type { NotificationResourceType, NotificationType } from '@/features/notifications/DTOs/notification.dtos'

const isOpen = ref(false)
const target = ref<HTMLElement | null>(null)
const queryClient = useQueryClient()

const { data, isLoading, isError, error } = useMyNotificationsQuery()
const acceptMutation = useAcceptNotificationMutation()
const rejectMutation = useRejectNotificationMutation()
const readMutation = useMarkNotificationAsReadMutation()
const isProcessing = computed(() => acceptMutation.isPending.value || rejectMutation.isPending.value || readMutation.isPending.value)

const pendingNotifications = computed(() => data.value?.filter(notification => notification.status === 'PENDING') ?? [])
const pendingCount = computed(() => pendingNotifications.value.length)

function closeMenu() {
  isOpen.value = false
}

function toggleMenu() {
  isOpen.value = !isOpen.value
}

function notificationLabel(type: NotificationType) {
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

function isTaskNotification(type: NotificationType) {
  return type === 'TASK_ASSIGNED' || type === 'TASK_STATUS_CHANGED'
}

function accept(notificationId: number) {
  acceptMutation.mutateAsync(notificationId)
    .then(async () => {
      await queryClient.refetchQueries({ queryKey: ['notifications'] })
      await queryClient.invalidateQueries({ queryKey: ['projects'] })
      await queryClient.invalidateQueries({ queryKey: ['project'] })
      await queryClient.invalidateQueries({ queryKey: ['projects', 'teams'] })
      closeMenu()
      toast.success('Notification accepted')
    })
    .catch((mutationError) => {
      toast.error(mutationError instanceof Error ? mutationError.message : 'Failed to accept notification')
    })
}

function reject(notificationId: number) {
  rejectMutation.mutateAsync(notificationId)
    .then(async () => {
      await queryClient.refetchQueries({ queryKey: ['notifications'] })
      closeMenu()
      toast.success('Notification rejected')
    })
    .catch((mutationError) => {
      toast.error(mutationError instanceof Error ? mutationError.message : 'Failed to reject notification')
    })
}

function markAsRead(notificationId: number) {
  readMutation.mutateAsync(notificationId)
    .then(async () => {
      await queryClient.refetchQueries({ queryKey: ['notifications'] })
      closeMenu()
      toast.success('Notification marked as read')
    })
    .catch((mutationError) => {
      toast.error(mutationError instanceof Error ? mutationError.message : 'Failed to mark notification as read')
    })
}

onClickOutside(target, () => {
  closeMenu()
})
</script>