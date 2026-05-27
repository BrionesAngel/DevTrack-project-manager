<template>
  <div class="flex flex-col gap-2">
    <button
      type="button"
      class="flex items-center justify-center gap-2 rounded-md border-2 border-slate-300 px-3 py-1 text-sm font-medium transition cursor-pointer sm:px-4 sm:text-lg"
      :class="open
        ? 'bg-blue-300 text-blue-900 hover:bg-blue-900 hover:text-blue-200'
        : 'bg-blue-300 text-blue-900 hover:bg-blue-900 hover:text-blue-200'"
      @click="open = !open">
      <UserPlusIcon class="size-5" />
      <span class="text-sm font-medium sm:text-lg">{{ buttonLabel }}</span>
    </button>

    <div v-if="open" class="flex flex-col gap-2 rounded-md border border-slate-300 bg-white p-3 md:flex-row md:items-center">
      <input
        v-model="username"
        type="text"
        class="w-full rounded-md border border-slate-300 px-3 py-2 outline-none ring-0 focus:border-blue-700"
        placeholder="Username"
        @keyup.enter="sendInvitation"
      />
      <button
        type="button"
        class="rounded-md border-2 bg-blue-300 px-4 py-1 text-lg font-medium text-blue-900 transition hover:bg-blue-900 hover:text-blue-200 disabled:cursor-not-allowed disabled:opacity-60"
        :disabled="isPending"
        @click="sendInvitation">
        Send
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { toast } from 'vue-sonner'
import { UserPlusIcon } from '@lucide/vue'
import { useCreateNotificationMutation } from '../queries/notification.queries'
import type { NotificationResourceType, NotificationType } from '../DTOs/notification.dtos'

const props = defineProps<{
  buttonLabel: string
  successMessage: string
  notificationType: NotificationType
  resourceType: NotificationResourceType
  resourceId: number
}>()

const open = ref(false)
const username = ref('')

const { mutate, isPending } = useCreateNotificationMutation()

function sendInvitation() {
  const normalizedUsername = username.value.trim()

  if (!normalizedUsername) {
    toast.error('Username is required')
    return
  }

  mutate({
    username: normalizedUsername,
    type: props.notificationType,
    resourceType: props.resourceType,
    resourceId: props.resourceId,
  }, {
    onSuccess: () => {
      toast.success(props.successMessage)
      username.value = ''
      open.value = false
    },
    onError: (error) => {
      toast.error(error instanceof Error ? error.message : 'Failed to send invitation')
    },
  })
}
</script>