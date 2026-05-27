<template>
  <section class="mx-auto flex flex-col w-full max-w-5xl">
    <div class="flex flex-col gap-4 m-8 p-4 bg-white border border-cyan-200 rounded-lg shadow-md">
      <div class="flex flex-row items-center bg-cyan-50 rounded-lg border-cyan-200 border-2 border-dashed">
        <BackButton :fallback="backFallback" />
        <h2 class="flex w-full justify-center py-4 items-center text-3xl text-cyan-900">Create Task</h2>
      </div>

      <p v-if="isFixedProject" class="mb-4 text-slate-700">Project selected: {{ selectedProjectId }}</p>

      <div v-if="!isFixedProject" class="mb-4">
        <label class="mb-1 block text-sm font-medium text-slate-800" for="project">Project</label>
        <select
          id="project"
          v-model.number="selectedProjectId"
          class="w-full rounded-lg border border-slate-300 px-3 py-2 outline-none ring-cyan-400 focus:ring">
          <option :value="null" disabled>Select a project</option>
          <option v-for="project in projectsWithTeams ?? []" :key="project.id" :value="project.id">
            {{ project.title }}
          </option>
        </select>
      </div>

      <form @submit.prevent="onSubmit" class="flex flex-col gap-8 p-4">
        <div>
          <label class="block text-sm font-medium text-slate-800" for="title">Title</label>
          <input id="title" v-model="title" type="text" required
            class="w-full rounded-lg border border-slate-300 px-3 py-2 outline-none ring-cyan-400 focus:ring" />
        </div>

        <div>
          <label class="block text-sm font-medium text-slate-800" for="description">Description</label>
          <textarea id="description" v-model="description" rows="4"
            class="w-full rounded-lg border border-slate-300 px-3 py-2 outline-none ring-cyan-400 focus:ring" />
        </div>

        <div>
          <label class="block text-sm font-medium text-slate-800" for="priority">Priority</label>
          <select id="priority" v-model="priority"
            class="w-full rounded-lg border border-slate-300 px-3 py-2 outline-none ring-cyan-400 focus:ring">
            <option value="LOW">Low</option>
            <option value="MEDIUM">Medium</option>
            <option value="HIGH">High</option>
          </select>
        </div>

        <div>
          <label class="block text-sm font-medium text-slate-800" for="team">Team</label>
          <select
            id="team"
            v-model.number="selectedTeamId"
            required
            class="w-full rounded-lg border border-slate-300 px-3 py-2 outline-none ring-cyan-400 focus:ring">
            <option :value="null" disabled>Select a team</option>
            <option v-for="team in selectedProject?.teams ?? []" :key="team.id" :value="team.id">
              {{ team.name }}
            </option>
          </select>
        </div>

        <div>
          <label class="block text-sm font-medium text-slate-800" for="dueDate">Due date</label>
          <input
            id="dueDate"
            v-model="dueDate"
            type="date"
            class="w-full rounded-lg border border-slate-300 px-3 py-2 outline-none ring-cyan-400 focus:ring" />
        </div>

        <div>
          <label class="block text-sm font-medium text-slate-800" for="githubIssueUrl">GitHub issue URL</label>
          <input
            id="githubIssueUrl"
            v-model="githubIssueUrl"
            type="url"
            placeholder="https://github.com/org/repo/issues/123"
            class="w-full rounded-lg border border-slate-300 px-3 py-2 outline-none ring-cyan-400 focus:ring" />
        </div>

        <div>
          <label class="block text-sm font-medium text-slate-800" for="initialComment">Initial comment (optional)</label>
          <textarea
            id="initialComment"
            v-model="initialComment"
            rows="3"
            placeholder="Context or notes for assignee/reviewer"
            class="w-full rounded-lg border border-slate-300 px-3 py-2 outline-none ring-cyan-400 focus:ring" />
        </div>

        <div class="flex justify-end-safe gap-6 mt-4">
          <button type="button" @click="onCancel"
            class="rounded-lg bg-cyan-600 px-4 py-2 font-medium text-white hover:bg-cyan-800 disabled:cursor-not-allowed disabled:opacity-60">Cancel</button>
          <button type="submit"
            class="rounded-lg bg-cyan-600 px-4 py-2 font-medium text-white hover:bg-cyan-800 disabled:cursor-not-allowed disabled:opacity-60"
            :disabled="isSaving || !selectedProjectId || !selectedTeamId">
            {{ isSaving ? 'Creating...' : 'Create Task' }}
          </button>
        </div>
      </form>
    </div>
  </section>
</template>

<script setup lang="ts">
import { computed, ref, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import BackButton from '@/shared/components/BackButton.vue';
import { useGetAllProjectsWithTeamsQuery } from '@/features/projects/queries/project.queries';
import { useCreateTaskMutation } from '../queries/task.queries';
import type { TaskPriority } from '../DTOs/task.dtos';
import { toast } from 'vue-sonner';

const route = useRoute()
const router = useRouter()

const { data: projectsWithTeams } = useGetAllProjectsWithTeamsQuery()
const createTaskMutation = useCreateTaskMutation()

const routeProjectId = computed(() => {
  const raw = route.params.projectId
  const parsed = Number(raw)
  return Number.isFinite(parsed) && parsed > 0 ? parsed : null
})

const selectedProjectId = ref<number | null>(routeProjectId.value)
const selectedTeamId = ref<number | null>(null)

const isFixedProject = computed(() => routeProjectId.value !== null)
const selectedProject = computed(() =>
  (projectsWithTeams.value ?? []).find(project => project.id === selectedProjectId.value)
)

const backFallback = computed(() =>
  routeProjectId.value !== null
    ? { name: 'project-details', params: { projectId: routeProjectId.value } }
    : 'tasks'
)

const title = ref('')
const description = ref('')
const priority = ref<TaskPriority>('MEDIUM')
const dueDate = ref('')
const githubIssueUrl = ref('')
const initialComment = ref('')
const isSaving = computed(() => createTaskMutation.isPending.value)

watch(selectedProjectId, () => {
  selectedTeamId.value = null
})

function onCancel() {
  if (routeProjectId.value !== null) {
    router.push({ name: 'project-details', params: { projectId: routeProjectId.value } })
    return
  }
  router.push({ name: 'tasks' })
}

async function onSubmit() {
  if (!selectedProjectId.value || !selectedTeamId.value) {
    toast.error('Project and team are required')
    return
  }

  const normalizedTitle = title.value.trim()
  const normalizedDescription = description.value.trim()

  if (!normalizedTitle || !normalizedDescription) {
    toast.error('Title and description are required')
    return
  }

  try {
    const createdTask = await createTaskMutation.mutateAsync({
      projectId: selectedProjectId.value,
      payload: {
        title: normalizedTitle,
        description: normalizedDescription,
        priority: priority.value,
        dueDate: dueDate.value || null,
        githubIssueUrl: githubIssueUrl.value.trim() || null,
        comment: initialComment.value.trim() || null,
        assignedUser: null,
        teamId: selectedTeamId.value,
      },
    })

    toast.success('Task created successfully')
    if (createdTask.assignedUserId) {
      router.push({
        name: 'project-kanban-view',
        query: {
          projectId: selectedProjectId.value,
          teamId: selectedTeamId.value,
          userId: createdTask.assignedUserId,
        },
      })
      return
    }

    router.push({
      name: 'team-details',
      params: {
        projectId: selectedProjectId.value,
        teamId: selectedTeamId.value,
      },
    })
  } catch (error) {
    toast.error(error instanceof Error ? error.message : 'Failed to create task')
  }
}
</script>
