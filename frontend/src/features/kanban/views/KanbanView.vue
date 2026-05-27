<template>
  <div class="flex h-full min-h-0 flex-1 flex-col items-stretch gap-3 p-2 pb-16">
    <div class="flex w-full flex-col gap-3 rounded-t-2xl border bg-white px-4 py-3 sm:flex-row sm:items-center sm:justify-between">
      <div class="text-center sm:text-left">
        <p class="text-xs font-semibold uppercase tracking-[0.22em] text-slate-500">Personal workspace</p>
        <h1 class="text-2xl font-black text-slate-800 sm:text-3xl">My Kanban</h1>
        <p class="text-sm text-slate-500">All your tasks in one place.</p>
      </div>

      <div class="flex flex-wrap justify-center gap-2 text-sm text-slate-600 sm:justify-end">
        <span class="rounded-full bg-slate-100 px-3 py-1 font-medium">Projects {{ projectCount }}</span>
        <span class="rounded-full bg-slate-100 px-3 py-1 font-medium">Tasks {{ visibleTasks.length }}</span>
      </div>
    </div>

    <div v-if="isLoading" class="mt-4 w-full rounded-md border border-slate-200 bg-white p-6 text-slate-500">
      Loading kanban...
    </div>

    <div v-else-if="isError" class="mt-4 w-full rounded-md border border-rose-200 bg-rose-50 p-6 text-rose-700">
      Failed to load kanban.
      {{ error?.message }}
    </div>

    <div v-else-if="visibleTasks.length === 0" class="mt-4 w-full rounded-md border border-amber-200 bg-amber-50 p-6 text-amber-800">
      No tasks were found across your projects yet.
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
            <div v-for="task in column.tasks" :key="`${task.projectId}-${task.id}`">
              <TaskCard :task="task" @open="openTask" />
            </div>

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

  <div class="fixed bottom-0 left-0 right-0 z-50 flex h-16 items-center justify-evenly gap-2 border-t border-slate-200 bg-slate-100 text-sm shadow-xl shadow-zinc-800 lg:hidden md:text-lg">
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

</template>

<script setup lang="ts">
import { computed, onBeforeUnmount, onMounted, ref } from 'vue'
import { useQuery } from '@tanstack/vue-query'
import TaskCard from '@/features/tasks/components/TaskCard.vue'
import { useRouter } from 'vue-router'
import { projectService } from '@/features/projects/services/project.service'
import { taskService } from '@/features/tasks/services/task.service'
import type { TaskResponse } from '@/features/tasks/DTOs/task.dtos'
import { useUserProfileQuery } from '@/features/users/queries/users.querys'

const router = useRouter()
const activeCol = ref(1)
const isLg = ref(window.innerWidth >= 1024)

const { data: profile } = useUserProfileQuery()
const currentUserId = computed(() => profile.value?.id ?? 0)

function updateViewport() {
  isLg.value = window.innerWidth >= 1024
}

onMounted(() => window.addEventListener('resize', updateViewport))
onBeforeUnmount(() => window.removeEventListener('resize', updateViewport))

const myKanbanQuery = useQuery({
  queryKey: computed(() => ['kanban', 'my', currentUserId.value]),
  queryFn: async () => {
    if (!currentUserId.value) {
      return { projects: [], tasks: [] as TaskResponse[] }
    }

    const projects = await projectService.getAllProjects()
    const tasksByProject = await Promise.all(
      projects.map(async (project) => {
        return taskService.getProjectTasks(project.id, { assignedUserId: currentUserId.value })
      }),
    )

    return {
      projects,
      tasks: tasksByProject.flat(),
    }
  },
  enabled: currentUserId.value > 0,
  staleTime: 1000 * 60 * 5,
})

const isLoading = computed(() => myKanbanQuery.isLoading.value || currentUserId.value === 0)
const isError = computed(() => myKanbanQuery.isError.value)
const error = computed(() => myKanbanQuery.error.value)

const projectCount = computed(() => myKanbanQuery.data.value?.projects.length ?? 0)
const visibleTasks = computed(() => myKanbanQuery.data.value?.tasks ?? [])

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

function openTask(task: TaskResponse | null | undefined) {
  if (!task) return
  const query: Record<string, string> = {
    projectId: String(task.projectId),
    kanbanView: 'global',
  }

  if (task.assignedTeamId != null) {
    query.teamId = String(task.assignedTeamId)
  }

  router.push({
    name: 'task-details',
    params: { projectId: task.projectId, taskId: task.id },
    query,
  })
}
</script>
