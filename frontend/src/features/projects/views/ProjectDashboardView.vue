<template>
  <div class="mx-auto flex w-full max-w-6xl flex-1 flex-col gap-6 p-4">
    <section class="overflow-hidden rounded-2xl border border-slate-200 bg-white shadow-sm">
      <div class="bg-linear-to-r from-slate-900 via-indigo-950 to-slate-900 px-5 py-6 text-white sm:px-6">
        <p class="text-xs font-semibold uppercase tracking-[0.28em] text-indigo-200">Workspace</p>
        <div class="mt-2 flex flex-col gap-3 sm:flex-row sm:items-end sm:justify-between">
          <div>
            <h1 class="text-3xl font-black tracking-tight sm:text-4xl">Dashboard</h1>
            <p class="mt-2 max-w-2xl text-sm text-slate-300 sm:text-base">
              A quick view of your recent projects and task flow, aligned with the kanban states.
            </p>
          </div>

          <CreateProjectButton class="shrink-0" />
        </div>
      </div>

      <div class="grid gap-3 border-t border-slate-200 bg-slate-50 p-4 sm:grid-cols-2 xl:grid-cols-5">
        <div
          v-for="card in taskMetricCards"
          :key="card.label"
          class="rounded-xl border border-slate-200 bg-white p-4 shadow-sm transition hover:-translate-y-0.5 hover:shadow-md">
          <div class="flex items-start justify-between gap-3">
            <div>
              <p class="text-sm font-semibold uppercase tracking-[0.18em] text-slate-500">{{ card.label }}</p>
              <p class="mt-2 text-3xl font-black text-slate-900">{{ card.value }}</p>
            </div>
            <span class="rounded-full px-3 py-1 text-xs font-semibold" :class="card.badgeClass">
              {{ card.shortLabel }}
            </span>
          </div>
          <p class="mt-3 text-sm text-slate-500">{{ card.description }}</p>
        </div>
      </div>
    </section>

    <section class="flex flex-col gap-4">
      <div class="flex flex-col gap-2 sm:flex-row sm:items-end sm:justify-between">
        <div>
          <p class="text-xs font-semibold uppercase tracking-[0.22em] text-slate-500">Projects</p>
          <h2 class="text-2xl font-black text-slate-900">Recent projects</h2>
        </div>
        <p class="text-sm text-slate-500">Showing your latest three projects.</p>
      </div>

      <div v-if="isLoading" class="rounded-xl border border-slate-200 bg-white p-6 text-slate-500">
        Loading dashboard...
      </div>

      <div v-else-if="isError" class="rounded-xl border border-rose-200 bg-rose-50 p-6 text-rose-700">
        Failed to load dashboard.
        {{ error?.message }}
      </div>

      <div v-else-if="recentProjects.length === 0" class="rounded-2xl border border-dashed border-slate-300 bg-white p-10 text-center">
        <p class="text-2xl font-bold text-slate-800">You are not part of any project yet.</p>
        <p class="mt-2 text-slate-500">Start by creating one and then you will see the dashboard come alive.</p>
        <div class="mt-6 flex justify-center">
          <CreateProjectButton />
        </div>
      </div>

      <ul v-else class="grid grid-cols-1 gap-4 md:grid-cols-2 xl:grid-cols-3">
        <li v-for="project in recentProjects" :key="project.id">
          <ProjectCard :with-tasks-shortcut="true" :project="project" />
        </li>
      </ul>
    </section>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useQuery } from '@tanstack/vue-query'
import ProjectCard from '@/shared/components/ProjectCard.vue'
import CreateProjectButton from '../components/CreateProjectButton.vue'
import { projectService } from '../services/project.service'
import { taskService } from '@/features/tasks/services/task.service'
import type { TaskResponse } from '@/features/tasks/DTOs/task.dtos'

type DashboardTaskCard = {
  label: string
  shortLabel: string
  value: number
  description: string
  badgeClass: string
}

const dashboardQuery = useQuery({
  queryKey: ['dashboard', 'summary'],
  queryFn: async () => {
    const projects = await projectService.getAllProjects()
    const recentProjects = projects.slice(-3).reverse()

    const recentProjectTasks = await Promise.all(
      recentProjects.map(async (project) => ({
        projectId: project.id,
        tasks: await taskService.getProjectTasks(project.id),
      }))
    )

    return {
      projects,
      recentProjects,
      recentTasks: recentProjectTasks.flatMap(entry => entry.tasks),
    }
  },
  staleTime: 1000 * 60 * 5,
})

const isLoading = computed(() => dashboardQuery.isLoading.value)
const isError = computed(() => dashboardQuery.isError.value)
const error = computed(() => dashboardQuery.error.value)

const recentProjects = computed(() => dashboardQuery.data.value?.recentProjects ?? [])
const recentTasks = computed(() => dashboardQuery.data.value?.recentTasks ?? [])

function isDelayed(task: TaskResponse) {
  if (!task.dueDate) return false
  if (task.status === 'COMPLETED' || task.status === 'REJECTED') return false

  const dueDate = new Date(task.dueDate)
  dueDate.setHours(23, 59, 59, 999)
  return dueDate.getTime() < Date.now()
}

const taskMetricCards = computed<DashboardTaskCard[]>(() => [
  {
    label: 'Pending',
    shortLabel: 'ASSIGNED',
    value: recentTasks.value.filter(task => task.status === 'ASSIGNED').length,
    description: 'Tasks assigned but not started yet.',
    badgeClass: 'bg-amber-100 text-amber-800',
  },
  {
    label: 'In progress',
    shortLabel: 'ACTIVE',
    value: recentTasks.value.filter(task => task.status === 'IN_PROGRESS').length,
    description: 'Tasks currently being worked on.',
    badgeClass: 'bg-blue-100 text-blue-800',
  },
  {
    label: 'Review',
    shortLabel: 'CHECK',
    value: recentTasks.value.filter(task => task.status === 'REVIEW').length,
    description: 'Tasks waiting on review or approval.',
    badgeClass: 'bg-orange-100 text-orange-800',
  },
  {
    label: 'Rejected',
    shortLabel: 'BLOCKED',
    value: recentTasks.value.filter(task => task.status === 'REJECTED').length,
    description: 'Tasks that need a retry or correction.',
    badgeClass: 'bg-rose-100 text-rose-800',
  },
  {
    label: 'Delayed',
    shortLabel: 'OVERDUE',
    value: recentTasks.value.filter(isDelayed).length,
    description: 'Tasks that passed the due date and are still open.',
    badgeClass: 'bg-slate-200 text-slate-700',
  },
])
</script>
