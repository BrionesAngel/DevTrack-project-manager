<template>
  <div class="fixed inset-0 z-60 flex items-start justify-center p-4">
    <div class="absolute inset-0 bg-black/40" @click="close"></div>

    <div class="relative z-70 w-full max-w-2xl rounded-md bg-white p-4 shadow-lg">
      <div class="flex items-start justify-between gap-4">
        <div>
          <h3 class="text-lg font-bold text-slate-800">{{ task.title }}</h3>
          <p class="text-sm text-slate-500">{{ task.description }}</p>
        </div>
        <button class="text-slate-500" @click="close">✕</button>
      </div>

      <div class="mt-4 grid grid-cols-2 gap-4">
        <div>
          <p class="text-xs text-slate-500">Assigned</p>
          <p class="text-sm text-slate-800">{{ task.assignedUsername ?? 'Unassigned' }}</p>
        </div>
        <div>
          <p class="text-xs text-slate-500">Status</p>
          <p class="text-sm text-slate-800 uppercase">{{ task.status }}</p>
        </div>
      </div>

      <div class="mt-4 flex flex-col gap-2">
        <div class="text-sm text-slate-600">Actions</div>

        <div class="flex flex-wrap gap-2">
          <button
            v-if="canTake"
            :disabled="updating"
            @click="takeTask"
            class="rounded-md bg-slate-800 px-3 py-2 text-white">
            Take task
          </button>

          <button
            v-if="canFinish"
            :disabled="updating"
            @click="finishTask"
            class="rounded-md bg-amber-700 px-3 py-2 text-white">
            Finish & request review
          </button>

          <button
            v-if="canAccept"
            :disabled="updating"
            @click="acceptTask"
            class="rounded-md bg-lime-600 px-3 py-2 text-white">
            Accept
          </button>

          <button
            v-if="canReject"
            :disabled="updating"
            @click="rejectTask"
            class="rounded-md bg-red-600 px-3 py-2 text-white">
            Reject
          </button>

          <button
            v-if="canRetake"
            :disabled="updating"
            @click="takeTask"
            class="rounded-md bg-slate-800 px-3 py-2 text-white">
            Retake
          </button>
        </div>
      </div>

      <div class="mt-4 flex justify-end">
        <button class="rounded-md border px-3 py-2" @click="close">Close</button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { useUpdateTaskMutation, useUpdateTaskAssigneeMutation } from '@/features/tasks/queries/task.queries'
import { toast } from 'vue-sonner'
import type { TaskResponse, TaskUpdateRequest } from '../DTOs/task.dtos'
import { useUserProfileQuery } from '@/features/users/queries/users.querys'

const props = defineProps<{
  task: TaskResponse
  isTeamLead?: boolean
  isProjectAdmin?: boolean
}>()

const emit = defineEmits<{
  (e: 'close'): void
}>()

const { data: profile } = useUserProfileQuery()
const mutation = useUpdateTaskMutation()
const assigneeMutation = useUpdateTaskAssigneeMutation()
const updating = ref(false)

const isAssignee = computed(() => profile.value?.id && props.task.assignedUserId === profile.value?.id)
const isLeader = computed(() => props.isTeamLead)

const canTake = computed(() => isAssignee.value && (props.task.status === 'ASSIGNED' || props.task.status === 'REJECTED'))
const canFinish = computed(() => isAssignee.value && props.task.status === 'IN_PROGRESS')
const canAccept = computed(() => isLeader.value && props.task.status === 'REVIEW')
const canReject = computed(() => isLeader.value && props.task.status === 'REVIEW')
const canRetake = computed(() => isAssignee.value && props.task.status === 'REJECTED')

function close() {
  emit('close')
}

async function runUpdate(payload: TaskUpdateRequest) {
  try {
    updating.value = true
    // ensure the current user is assigned before changing status
    const myId = profile.value?.id
    if (myId && props.task.assignedUserId !== myId) {
      try {
        await assigneeMutation.mutateAsync({
          projectId: props.task.projectId,
          taskId: props.task.id,
          payload: { assignedUserId: myId },
        })
      } catch {
        // ignore - we'll surface the main error below if status update still fails
      }
    }

    await mutation.mutateAsync({ projectId: props.task.projectId, taskId: props.task.id, payload })
    toast.success('Task updated')
    emit('close')
  } catch (err) {
    const message = err instanceof Error ? err.message : 'Failed to update task'
    toast.error(message)
  } finally {
    updating.value = false
  }
}

function takeTask() {
  runUpdate({ status: 'IN_PROGRESS' })
}

function finishTask() {
  runUpdate({ status: 'REVIEW' })
}

function acceptTask() {
  runUpdate({ status: 'COMPLETED' })
}

function rejectTask() {
  runUpdate({ status: 'REJECTED' })
}
</script>
