<template>
  <div class="w-full px-4 py-6 sm:px-6 lg:px-8">
    <div v-if="!hasProjectId" class="mx-auto w-full max-w-4xl rounded-2xl border border-amber-200 bg-amber-50 p-6 text-amber-800 shadow-sm">
      Select a project
    </div>

    <div v-else-if="!task" class="mx-auto w-full max-w-4xl rounded-2xl border border-slate-200 bg-white p-6 text-slate-500 shadow-sm">
      Task not found
    </div>

    <div v-else class="mx-auto w-full max-w-5xl">
      <div class="mb-3 flex justify-start">
        <BackButton :fallback="backLink" />
      </div>

      <div class="overflow-hidden rounded-3xl border border-slate-200 bg-white shadow-2xl shadow-slate-300/60">
      <div class="bg-linear-to-r from-slate-900 via-indigo-950 to-slate-900 px-6 py-8 text-white sm:px-8">
        <div class="flex flex-col gap-4 lg:flex-row lg:items-start lg:justify-between">
          <div class="max-w-3xl">
            <p class="text-xs font-semibold uppercase tracking-[0.28em] text-indigo-200">Task details</p>
            <h2 class="mt-2 text-3xl font-black tracking-tight sm:text-4xl">{{ task.title }}</h2>
            <p class="mt-3 max-w-2xl text-sm leading-6 text-slate-300 sm:text-base">{{ task.description }}</p>
          </div>

          <div class="shrink-0 rounded-2xl border border-white/10 bg-white/10 px-4 py-3 text-sm backdrop-blur-sm">
            <p class="text-xs uppercase tracking-[0.2em] text-slate-300">Status</p>
            <strong class="mt-1 block text-lg uppercase">{{ task.status }}</strong>
          </div>
        </div>
      </div>

      <div class="grid gap-6 px-6 py-6 sm:px-8">
        <div class="space-y-6">
          <div class="grid gap-4 sm:grid-cols-2 xl:grid-cols-4">
            <div class="rounded-2xl border border-slate-200 bg-slate-50 p-4">
              <p class="text-xs font-semibold uppercase tracking-[0.18em] text-slate-500">Assigned</p>
              <p class="mt-2 text-base font-semibold text-slate-900">{{ task.assignedUsername ?? 'Unassigned' }}</p>
            </div>
            <div class="rounded-2xl border border-slate-200 bg-slate-50 p-4">
              <p class="text-xs font-semibold uppercase tracking-[0.18em] text-slate-500">Due</p>
              <p class="mt-2 text-base font-semibold text-slate-900">{{ task.dueDate ?? '—' }}</p>
            </div>
            <div class="rounded-2xl border border-slate-200 bg-slate-50 p-4">
              <p class="text-xs font-semibold uppercase tracking-[0.18em] text-slate-500">Project</p>
              <p class="mt-2 text-base font-semibold text-slate-900">{{ projectData?.title ?? `Project ${projectId}` }}</p>
            </div>
            <div class="rounded-2xl border border-slate-200 bg-slate-50 p-4">
              <p class="text-xs font-semibold uppercase tracking-[0.18em] text-slate-500">Team</p>
              <p class="mt-2 text-base font-semibold text-slate-900">{{ task.assignedTeamName || 'No team' }}</p>
            </div>
          </div>

          <div class="rounded-2xl border border-slate-200 bg-white p-4 shadow-sm">
            <p class="text-xs font-semibold uppercase tracking-[0.18em] text-slate-500">GitHub issue</p>
            <a
              v-if="task.githubIssueUrl"
              :href="task.githubIssueUrl"
              target="_blank"
              rel="noopener noreferrer"
              class="mt-2 inline-block text-sm font-semibold text-blue-700 hover:text-blue-900 hover:underline">
              {{ task.githubIssueUrl }}
            </a>
            <p v-else class="mt-2 text-sm text-slate-500">No GitHub issue linked.</p>
          </div>

          <div class="rounded-2xl border border-slate-200 bg-white p-4 shadow-sm">
            <div class="mb-3 flex items-center justify-between gap-3">
              <div>
                <p class="text-xs font-semibold uppercase tracking-[0.18em] text-slate-500">Actions</p>
                <p class="mt-1 text-sm text-slate-500">Only the assignee or lead can change the task state.</p>
              </div>
            </div>

            <div class="mb-3">
              <label class="mb-1 block text-xs font-semibold uppercase tracking-[0.15em] text-slate-500" for="transitionComment">
                Comment for transition
              </label>
              <textarea
                id="transitionComment"
                v-model="transitionComment"
                rows="3"
                placeholder="Explain what changed before sending to review / accepting / rejecting"
                class="w-full rounded-lg border border-slate-300 px-3 py-2 text-sm outline-none ring-slate-400 focus:ring" />
            </div>

            <div class="flex flex-wrap gap-2">
              <button v-if="canTake" @click="takeTask" :disabled="updating" class="rounded-full bg-slate-900 px-4 py-2 text-sm font-semibold text-white transition hover:bg-slate-700 disabled:cursor-not-allowed disabled:opacity-60">Take task</button>
              <button v-if="canFinish" @click="finishTask" :disabled="updating" class="rounded-full bg-amber-700 px-4 py-2 text-sm font-semibold text-white transition hover:bg-amber-600 disabled:cursor-not-allowed disabled:opacity-60">Finish & request review</button>
              <button v-if="canAccept" @click="acceptTask" :disabled="updating" class="rounded-full bg-lime-600 px-4 py-2 text-sm font-semibold text-white transition hover:bg-lime-500 disabled:cursor-not-allowed disabled:opacity-60">Accept</button>
              <button v-if="canReject" @click="rejectTask" :disabled="updating" class="rounded-full bg-rose-600 px-4 py-2 text-sm font-semibold text-white transition hover:bg-rose-500 disabled:cursor-not-allowed disabled:opacity-60">Reject</button>
              <button v-if="canRetake" @click="takeTask" :disabled="updating" class="rounded-full bg-slate-900 px-4 py-2 text-sm font-semibold text-white transition hover:bg-slate-700 disabled:cursor-not-allowed disabled:opacity-60">Retake</button>
            </div>
          </div>

          <div class="rounded-2xl border border-slate-200 bg-white p-4 shadow-sm">
            <div class="mb-3 flex items-center justify-between gap-3">
              <div>
                <p class="text-xs font-semibold uppercase tracking-[0.18em] text-slate-500">Comments</p>
                <p class="mt-1 text-sm text-slate-500">Discussion and review history for this task.</p>
              </div>
            </div>

            <div class="mb-3">
              <textarea
                v-model="newComment"
                rows="3"
                placeholder="Add a comment"
                class="w-full rounded-lg border border-slate-300 px-3 py-2 text-sm outline-none ring-slate-400 focus:ring" />
              <div class="mt-2 flex justify-end">
                <button
                  type="button"
                  @click="addComment"
                  :disabled="isCommentSaving || !newComment.trim()"
                  class="rounded-full bg-slate-800 px-4 py-2 text-sm font-semibold text-white transition hover:bg-slate-700 disabled:cursor-not-allowed disabled:opacity-60">
                  {{ isCommentSaving ? 'Saving...' : 'Add comment' }}
                </button>
              </div>
            </div>

            <ul v-if="comments.length" class="space-y-2">
              <li v-for="comment in comments" :key="comment.id" class="rounded-lg border border-slate-200 bg-slate-50 p-3">
                <div class="flex items-center justify-between gap-2 text-xs text-slate-500">
                  <span class="font-semibold text-slate-700">{{ comment.authorUsername }}</span>
                  <span class="uppercase">{{ comment.action }}</span>
                </div>
                <p class="mt-2 text-sm text-slate-800">{{ comment.message }}</p>
                <p class="mt-2 text-xs text-slate-500">{{ formatDateTime(comment.createdAt) }}</p>
              </li>
            </ul>
            <p v-else class="text-sm text-slate-500">No comments yet.</p>
          </div>
        </div>
      </div>
    </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import BackButton from '@/shared/components/BackButton.vue'
import {
  useCreateTaskCommentMutation,
  useGetProjectTasksQuery,
  useGetTaskCommentsQuery,
} from '@/features/tasks/queries/task.queries'
import { useUserProfileQuery } from '@/features/users/queries/users.querys'
import { useGetTeamDetailsQuery } from '@/features/teams/queries/team.queries'
import { useGetProjectQuery } from '@/features/projects/queries/project.queries'
import { useUpdateTaskMutation, useUpdateTaskAssigneeMutation } from '@/features/tasks/queries/task.queries'
import { toast } from 'vue-sonner'
import type { TaskResponse, TaskUpdateRequest } from '../DTOs/task.dtos'
import { loadKanbanContext } from '@/features/kanban/composables/kanbanContext'
import type { ProjectMemberOverview } from '@/features/projects/DTOs/project.dtos'

const route = useRoute()
const router = useRouter()
const projectId = computed(() => Number(route.params.projectId))
const taskId = computed(() => Number(route.params.taskId))
const teamId = computed(() => Number(route.query.teamId))
const hasTeamId = computed(() => Number.isFinite(teamId.value) && teamId.value > 0)
const resolvedTeamId = computed(() => Number.isFinite(teamId.value) && teamId.value > 0 ? teamId.value : 0)
const hasProjectId = computed(() => Number.isFinite(projectId.value) && projectId.value > 0)

const { data: tasks } = useGetProjectTasksQuery(projectId.value)
const { data: profile } = useUserProfileQuery()
const { data: teamData } = useGetTeamDetailsQuery(projectId.value, resolvedTeamId.value)
const { data: projectData } = useGetProjectQuery(projectId.value)

const task = computed<TaskResponse | undefined>(() => tasks.value?.find(t => t.id === taskId.value))

const currentProjectMember = computed(() => projectData.value?.members.find((member: ProjectMemberOverview) => member.userId === profile.value?.id))
const currentTeamMember = computed(() => hasTeamId.value ? teamData.value?.members.find(member => member.userId === profile.value?.id) : undefined)
const isTeamLead = computed(() => currentTeamMember.value?.role === 'LEAD')
const canReviewWithoutTeam = computed(() => currentProjectMember.value?.role === 'OWNER' || currentProjectMember.value?.role === 'ADMIN')

const mutation = useUpdateTaskMutation()
const assigneeMutation = useUpdateTaskAssigneeMutation()
const commentMutation = useCreateTaskCommentMutation()
const updating = ref(false)
const transitionComment = ref('')
const newComment = ref('')

const { data: commentsData } = useGetTaskCommentsQuery(projectId.value, taskId.value)
const comments = computed(() => commentsData.value ?? [])
const isCommentSaving = computed(() => commentMutation.isPending.value)

const isAssignee = computed(() => profile.value?.id && task.value?.assignedUserId === profile.value?.id)
const isLeader = computed(() => isTeamLead.value)

const canTake = computed(() => isAssignee.value && (task.value?.status === 'ASSIGNED' || task.value?.status === 'REJECTED'))
const canFinish = computed(() => isAssignee.value && task.value?.status === 'IN_PROGRESS')
const canAccept = computed(() => (hasTeamId.value ? isLeader.value : canReviewWithoutTeam.value) && task.value?.status === 'REVIEW')
const canReject = computed(() => (hasTeamId.value ? isLeader.value : canReviewWithoutTeam.value) && task.value?.status === 'REVIEW')
const canRetake = computed(() => isAssignee.value && task.value?.status === 'REJECTED')

const backLink = computed(() => {
  if (route.query.kanbanView === 'global') {
    return { name: 'kanban' }
  }

  const hasKanbanQuery = route.query.projectId || route.query.teamId || route.query.userId

  if (hasKanbanQuery) {
    return {
      name: 'project-kanban-view',
      query: {
        ...(route.query.projectId ? { projectId: String(route.query.projectId) } : { projectId: String(projectId.value) }),
        ...(route.query.teamId ? { teamId: String(route.query.teamId) } : {}),
        ...(route.query.userId ? { userId: String(route.query.userId) } : {}),
      },
    }
  }

  const savedContext = loadKanbanContext()
  if (savedContext) {
    return {
      name: 'project-kanban-view',
      query: {
        projectId: String(savedContext.projectId),
        ...(savedContext.teamId ? { teamId: String(savedContext.teamId) } : {}),
        ...(savedContext.userId ? { userId: String(savedContext.userId) } : {}),
      },
    }
  }

  return { name: 'project-kanban-view', query: { projectId: String(projectId.value) } }
})

async function runUpdate(payload: TaskUpdateRequest) {
  try {
    updating.value = true
    // ensure current user is assigned before changing status
    const myId = profile.value?.id
    if (myId && task.value?.assignedUserId !== myId) {
      try {
        await assigneeMutation.mutateAsync({ projectId: projectId.value, taskId: taskId.value, payload: { assignedUserId: myId } })
      } catch {
        // ignore and continue to try status update
      }
    }

    await mutation.mutateAsync({
      projectId: projectId.value,
      taskId: taskId.value,
      payload: {
        ...payload,
        comment: transitionComment.value.trim() || undefined,
      },
    })
    transitionComment.value = ''
    toast.success('Task updated')
    router.replace(backLink.value)
  } catch (err) {
    const message = err instanceof Error ? err.message : 'Failed to update task'
    toast.error(message)
  } finally {
    updating.value = false
  }
}

function takeTask() { runUpdate({ status: 'IN_PROGRESS' }) }
function finishTask() { runUpdate({ status: 'REVIEW' }) }
function acceptTask() { runUpdate({ status: 'COMPLETED' }) }
function rejectTask() { runUpdate({ status: 'REJECTED' }) }

async function addComment() {
  const message = newComment.value.trim()
  if (!message) {
    return
  }

  try {
    await commentMutation.mutateAsync({
      projectId: projectId.value,
      taskId: taskId.value,
      payload: { message },
    })
    newComment.value = ''
    toast.success('Comment added')
  } catch (error) {
    toast.error(error instanceof Error ? error.message : 'Failed to add comment')
  }
}

function formatDateTime(value: string) {
  const date = new Date(value)
  if (Number.isNaN(date.getTime())) {
    return value
  }

  return date.toLocaleString()
}
</script>
