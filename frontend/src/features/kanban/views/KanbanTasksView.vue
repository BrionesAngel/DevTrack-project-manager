<template>
  <div class="flex h-full min-h-0 flex-1 flex-col items-stretch gap-3 p-2 pb-16">
    <div class="flex w-full justify-center rounded-t-2xl border bg-white">
      <BackButton :fallback="backFallback" />
      <div class="flex flex-9 flex-col items-center justify-center py-3 text-center">
        <span class="text-2xl font-bold text-slate-800">{{ boardTitle }}</span>
        <span class="text-sm text-slate-500">{{ boardSubtitle }}</span>
      </div>
    </div>

    <div v-if="!hasProjectId" class="mt-4 w-full rounded-md border border-amber-200 bg-amber-50 p-6 text-amber-800">
      Select a project to open its kanban.
    </div>

    <div v-else-if="!canAccessBoard" class="mt-4 w-full rounded-md border border-rose-200 bg-rose-50 p-6 text-rose-700">
      You do not have access to this kanban.
    </div>

    <div v-else-if="isLoading" class="mt-4 w-full rounded-md border border-slate-200 bg-white p-6 text-slate-500">
      Loading kanban...
    </div>

    <div v-else class="flex min-h-0 w-full flex-1 flex-col overflow-hidden border bg-white">
      <div class="flex flex-col gap-2 p-3 sm:flex-row sm:flex-wrap sm:items-center sm:justify-between">
        <div class="flex flex-wrap gap-2 text-sm font-medium text-slate-600">
          <span class="rounded-full bg-slate-100 px-3 py-1">Assigned {{ groupedTasks.ASSIGNED.length }}</span>
          <span class="rounded-full bg-amber-100 px-3 py-1 text-amber-800">In progress {{ groupedTasks.IN_PROGRESS.length }}</span>
          <span class="rounded-full bg-orange-100 px-3 py-1 text-orange-800">Review {{ groupedTasks.REVIEW.length }}</span>
          <span class="rounded-full bg-red-100 px-3 py-1 text-red-800">Rejected {{ groupedTasks.REJECTED.length }}</span>
          <span class="rounded-full bg-lime-100 px-3 py-1 text-lime-800">Completed {{ groupedTasks.COMPLETED.length }}</span>
        </div>
        <span class="text-sm text-slate-500">{{ visibleTasks.length }} tasks</span>
      </div>

      <div class="flex w-full flex-1 min-h-0 flex-col gap-2 overflow-hidden lg:flex-row">
        <section
          v-for="(column, index) in columns"
          :key="column.key"
          v-show="activeCol === index + 1 || isLg"
          class="flex flex-1 min-h-0 flex-col overflow-y-auto p-2"
          :class="column.columnClass">
          <span :class="[column.toneClasses, 'mb-2 flex justify-center rounded-md p-1.5 text-sm font-semibold']">
            {{ column.title }}
          </span>

          <div class="grid auto-rows-min grid-cols-1 gap-2 pr-1 sm:grid-cols-2 xl:grid-cols-3">
            <TaskCard v-for="task in column.tasks" :key="task.id" :task="task" @open="openTask" />
            <div
              v-if="!column.tasks.length"
              class="col-span-full rounded-md border border-dashed border-slate-300 bg-white p-4 text-center text-sm text-slate-500">
              No tasks in this column.
            </div>
          </div>
        </section>
      </div>
    </div>
  </div>

  <div
    v-if="hasProjectId && canAccessBoard"
    class="fixed bottom-0 left-0 right-0 z-50 flex h-16 items-center justify-evenly gap-2 border-t border-slate-200 bg-slate-100 text-sm shadow-xl shadow-zinc-800 lg:hidden md:text-lg">
    <button
      @click="activeCol = 1"
      :class="activeCol === 1 ? 'rounded-full bg-slate-800 px-3 py-2 font-semibold text-white shadow-sm' : 'rounded-full px-3 py-2 text-slate-600 transition hover:bg-slate-200'">
      assigned
    </button>
    <button
      @click="activeCol = 2"
      :class="activeCol === 2 ? 'rounded-full bg-slate-800 px-3 py-2 font-semibold text-white shadow-sm' : 'rounded-full px-3 py-2 text-slate-600 transition hover:bg-slate-200'">
      in progress
    </button>
    <button
      @click="activeCol = 3"
      :class="activeCol === 3 ? 'rounded-full bg-slate-800 px-3 py-2 font-semibold text-white shadow-sm' : 'rounded-full px-3 py-2 text-slate-600 transition hover:bg-slate-200'">
      review
    </button>
    <button
      @click="activeCol = 4"
      :class="activeCol === 4 ? 'rounded-full bg-slate-800 px-3 py-2 font-semibold text-white shadow-sm' : 'rounded-full px-3 py-2 text-slate-600 transition hover:bg-slate-200'">
      rejected
    </button>
    <button
      @click="activeCol = 5"
      :class="activeCol === 5 ? 'rounded-full bg-slate-800 px-3 py-2 font-semibold text-white shadow-sm' : 'rounded-full px-3 py-2 text-slate-600 transition hover:bg-slate-200'">
      completed
    </button>
  </div>

  <TaskDetailsModal v-if="selectedTask" :task="selectedTask" :isTeamLead="isTeamLead" :isProjectAdmin="isProjectAdmin" @close="closeTask" />
</template>

<script setup lang="ts">
import { computed, onBeforeUnmount, onMounted, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import BackButton from '@/shared/components/BackButton.vue'
import TaskCard from '@/features/tasks/components/TaskCard.vue'
import TaskDetailsModal from '@/features/tasks/components/TaskDetailsModal.vue'
import { useGetProjectQuery } from '@/features/projects/queries/project.queries'
import type { TaskResponse } from '@/features/tasks/DTOs/task.dtos'
import { useGetTeamDetailsQuery } from '@/features/teams/queries/team.queries'
import { useGetProjectTasksQuery } from '@/features/tasks/queries/task.queries'
import { useUserProfileQuery } from '@/features/users/queries/users.querys'
import { loadKanbanContext, saveKanbanContext } from '../composables/kanbanContext'

const route = useRoute()
const router = useRouter()

const activeCol = ref(1)
const isLg = ref(window.innerWidth >= 1024)

function updateViewport() {
  isLg.value = window.innerWidth >= 1024
}

onMounted(() => window.addEventListener('resize', updateViewport))
onBeforeUnmount(() => window.removeEventListener('resize', updateViewport))

const projectId = computed(() => Number(route.query.projectId))
const teamId = computed(() => Number(route.query.teamId))
const userId = computed(() => Number(route.query.userId))

const hasProjectId = computed(() => Number.isFinite(projectId.value) && projectId.value > 0)
const hasTeamId = computed(() => Number.isFinite(teamId.value) && teamId.value > 0)
const hasUserId = computed(() => Number.isFinite(userId.value) && userId.value > 0)

const { data: projectData, isLoading: isProjectLoading } = useGetProjectQuery(projectId.value)
const { data: teamData, isLoading: isTeamLoading } = useGetTeamDetailsQuery(projectId.value, teamId.value)
const { data: profile } = useUserProfileQuery()
const { data: tasks, isLoading: isTasksLoading } = useGetProjectTasksQuery(
  projectId.value,
  hasTeamId.value ? teamId.value : undefined,
  hasUserId.value ? userId.value : undefined,
)

const currentProjectMember = computed(() => projectData.value?.members.find(member => member.userId === profile.value?.id))
const currentTeamMember = computed(() => teamData.value?.members.find(member => member.userId === profile.value?.id))
const isProjectAdmin = computed(() => currentProjectMember.value?.role === 'OWNER' || currentProjectMember.value?.role === 'ADMIN')
const isTeamLead = computed(() => currentTeamMember.value?.role === 'LEAD')

const canAccessBoard = computed(() => {
  if (!hasProjectId.value) return false
  if (!hasTeamId.value) return true
  if (isProjectAdmin.value || isTeamLead.value) return true
  if (hasUserId.value && userId.value === profile.value?.id) return true
  return false
})

const visibleTasks = computed(() => tasks.value ?? [])
const groupedTasks = computed(() => ({
  ASSIGNED: visibleTasks.value.filter(task => task.status === 'ASSIGNED'),
  IN_PROGRESS: visibleTasks.value.filter(task => task.status === 'IN_PROGRESS'),
  REVIEW: visibleTasks.value.filter(task => task.status === 'REVIEW'),
  REJECTED: visibleTasks.value.filter(task => task.status === 'REJECTED'),
  COMPLETED: visibleTasks.value.filter(task => task.status === 'COMPLETED'),
}))

const columns = computed(() => [
  { key: 'ASSIGNED', title: 'tasks assigned', toneClasses: 'bg-slate-200 text-slate-800', columnClass: 'bg-slate-100', tasks: groupedTasks.value.ASSIGNED },
  { key: 'IN_PROGRESS', title: 'tasks in progress', toneClasses: 'bg-slate-300 text-slate-900', columnClass: 'bg-slate-200', tasks: groupedTasks.value.IN_PROGRESS },
  { key: 'REVIEW', title: 'tasks to review', toneClasses: 'bg-slate-200 text-slate-800', columnClass: 'bg-slate-100', tasks: groupedTasks.value.REVIEW },
  { key: 'REJECTED', title: 'tasks rejected', toneClasses: 'bg-slate-300 text-slate-900', columnClass: 'bg-slate-200', tasks: groupedTasks.value.REJECTED },
  { key: 'COMPLETED', title: 'tasks completed', toneClasses: 'bg-slate-200 text-slate-800', columnClass: 'bg-slate-100', tasks: groupedTasks.value.COMPLETED },
])

const boardTitle = computed(() => {
  if (hasUserId.value) return 'Member Kanban'
  if (hasTeamId.value) return 'Team Kanban'
  return 'Kanban'
})

const boardSubtitle = computed(() => {
  if (hasUserId.value) {
    const member = teamData.value?.members.find(item => item.userId === userId.value)?.username ?? `User ${userId.value}`
    return hasTeamId.value ? `${teamData.value?.name ?? 'Team'} · ${member}` : member
  }

  if (hasTeamId.value) {
    return teamData.value?.name ?? `Team ${teamId.value}`
  }

  return projectData.value?.title ?? `Project ${projectId.value}`
})

const backFallback = computed(() => {
  if (hasTeamId.value) {
    return { name: 'team-details', params: { projectId: projectId.value, teamId: teamId.value } }
  }

  if (hasProjectId.value) {
    return { name: 'project-details', params: { projectId: projectId.value } }
  }

  return 'kanban'
})

const isLoading = computed(() => isProjectLoading.value || isTeamLoading.value || isTasksLoading.value)

watch(
  () => [projectId.value, teamId.value, userId.value],
  ([currentProjectId, currentTeamId, currentUserId]) => {
    if (typeof currentProjectId === 'number' && Number.isFinite(currentProjectId) && currentProjectId > 0) {
      saveKanbanContext({
        projectId: currentProjectId,
        teamId: typeof currentTeamId === 'number' && Number.isFinite(currentTeamId) && currentTeamId > 0 ? currentTeamId : undefined,
        userId: typeof currentUserId === 'number' && Number.isFinite(currentUserId) && currentUserId > 0 ? currentUserId : undefined,
      })
    }
  },
  { immediate: true },
)

onMounted(() => {
  if (!hasProjectId.value) {
    const savedContext = loadKanbanContext()
    if (savedContext) {
      router.replace({
        name: 'project-kanban-view',
        query: {
          projectId: savedContext.projectId,
          ...(savedContext.teamId ? { teamId: savedContext.teamId } : {}),
          ...(savedContext.userId ? { userId: savedContext.userId } : {}),
        },
      })
    }
  }
})

const selectedTask = ref<TaskResponse | null>(null)

function openTask(task: TaskResponse | null | undefined) {
  if (!task) return
  // navigate to task details route
  if (hasProjectId.value) {
    router.push({
      name: 'task-details',
      params: { projectId: projectId.value, taskId: task.id },
      query: {
        ...(hasProjectId.value ? { projectId: String(projectId.value) } : {}),
        ...(hasTeamId.value ? { teamId: String(teamId.value) } : {}),
        ...(hasUserId.value ? { userId: String(userId.value) } : {}),
      },
    })
  }
  selectedTask.value = task
}

function closeTask() {
  selectedTask.value = null
}
</script>