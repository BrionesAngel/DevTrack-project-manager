<template>
  <section class="mx-auto flex w-full max-w-6xl flex-col gap-6 p-4">
    <div class="flex flex-col gap-3 sm:flex-row sm:items-center">
      <BackButton :fallback="{ name: 'project-details', params: { projectId } }" />
      <div class="min-w-0">
        <p class="text-xs font-semibold uppercase tracking-[0.2em] text-slate-500 sm:text-sm">Team details</p>
        <h1 class="truncate text-3xl font-black text-slate-800 sm:text-4xl">{{ data?.name ?? 'Team' }}</h1>
      </div>
      <div class="flex w-full flex-col gap-2 sm:ml-auto sm:w-auto sm:flex-row sm:flex-wrap sm:items-center sm:justify-end">
        <InviteUserButton
          v-if="canInviteToTeam"
          button-label="invite to team"
          success-message="Invitation sent to the team"
          notification-type="TEAM_INVITATION"
          resource-type="TEAM"
          :resource-id="teamId" />

        <AlertDialog v-if="canManageProject">
          <AlertDialogTrigger as-child>
            <button class="flex items-center justify-center gap-2 rounded-md border-2 border-slate-300 bg-red-300 px-3 py-1 text-sm font-medium text-red-900 transition hover:bg-red-900 hover:text-red-200 sm:text-lg sm:px-4">
              <Trash2Icon class="size-5" />
              <span class="text-sm font-medium sm:text-lg">delete team</span>
            </button>
          </AlertDialogTrigger>
          <AlertDialogContent>
            <AlertDialogHeader>
              <AlertDialogDescription class="text-lg">This action cannot be undone. The team and all its members will be permanently removed.</AlertDialogDescription>
            </AlertDialogHeader>
            <AlertDialogFooter>
              <AlertDialogCancel>Cancel</AlertDialogCancel>
              <AlertDialogAction @click="onDeleteTeam()">Delete</AlertDialogAction>
            </AlertDialogFooter>
          </AlertDialogContent>
        </AlertDialog>
      </div>
    </div>

    <div v-if="isLoading" class="rounded-md border border-slate-200 bg-white p-6 text-slate-500">
      Loading team...
    </div>

    <div v-else-if="isError" class="rounded-md border border-rose-200 bg-rose-50 p-6 text-rose-700">
      Failed to load team.
      {{ error?.message }}
    </div>

    <template v-else-if="data">
      <div class="rounded-md border border-slate-200 bg-white p-5 shadow-sm">
        <div class="flex items-center justify-between gap-4">
          <h2 class="text-xl font-black text-slate-800">Members</h2>
          <span class="rounded-full bg-slate-100 px-3 py-1 text-sm font-semibold text-slate-600">{{ data.members.length }}</span>
        </div>

        <ul class="mt-4 space-y-2">
          <li
            v-for="member in data.members"
            :key="member.userId"
            class="rounded-md border border-slate-100 bg-slate-50 px-3 py-3">
            <div class="flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
              <div class="min-w-0">
                <p class="truncate font-semibold text-slate-800">
                  {{ member.username }}
                  <span v-if="member.userId === currentUserId" class="ml-2 text-xs font-medium text-slate-500">you</span>
                </p>
              </div>

              <div class="flex flex-wrap items-center gap-2 sm:justify-end">
                <span class="shrink-0 rounded-full px-3 py-1 text-xs font-semibold" :class="memberBadgeClass(member.role)">
                  {{ member.role }}
                </span>

                <button
                  v-if="canViewMemberKanban(member.userId)"
                  type="button"
                  class="shrink-0 rounded-md border-2 bg-emerald-300 px-3 py-1 text-sm font-medium text-emerald-900 transition hover:bg-emerald-900 hover:text-emerald-100"
                  @click="goToMemberKanban(member.userId)">
                  {{ member.userId === currentUserId ? 'my kanban' : 'member kanban' }}
                </button>

                <template v-if="canManageProject">
                  <div class="flex flex-nowrap items-center gap-2">
                    <select
                      v-model="selectedMemberRoles[member.userId]"
                      class="w-auto max-w-24 flex-none rounded-md border border-slate-300 bg-white px-1 py-1 text-xs text-slate-700 outline-none focus:border-blue-700">
                      <option value="MEMBER">Member</option>
                      <option value="LEAD">Lead</option>
                    </select>

                    <button
                      type="button"
                      class="shrink-0 rounded-md border-2 bg-slate-300 px-2 py-1 text-xs font-medium text-slate-800 transition hover:bg-slate-800 hover:text-slate-200 disabled:opacity-60"
                      :disabled="isRoleMutationPending"
                      @click="updateTeamMemberRole(member.userId)">
                      Save
                    </button>
                  </div>
                </template>
              </div>
            </div>
          </li>
        </ul>
      </div>

      <div class="rounded-md border border-slate-200 bg-white p-5 shadow-sm">
        <div class="flex items-center justify-between gap-4">
          <h2 class="text-xl font-black text-slate-800">Unassigned team tasks</h2>
          <div class="flex items-center gap-2">
            <span class="rounded-full bg-slate-100 px-3 py-1 text-sm font-semibold text-slate-600">{{ teamTasks?.length ?? 0 }}</span>
            <button
              v-if="canViewTeamKanban"
              type="button"
              class="rounded-md border-2 bg-emerald-300 px-3 py-1 text-sm font-medium text-emerald-900 transition hover:bg-emerald-900 hover:text-emerald-100"
              @click="goToTeamKanban">
              team kanban
            </button>
          </div>
        </div>

        <ul class="mt-4 space-y-3">
          <li
            v-for="task in teamTasks ?? []"
            :key="task.id"
            class="rounded-md border border-slate-100 bg-slate-50 p-3">
            <div class="flex flex-col gap-2 lg:flex-row lg:items-center lg:justify-between">
              <div class="min-w-0">
                <p class="truncate text-lg font-bold text-slate-800">{{ task.title }}</p>
                <p class="text-sm text-slate-500">{{ task.description }}</p>
                <div class="mt-2 flex flex-wrap items-center gap-2 text-xs">
                  <span class="rounded-full bg-slate-200 px-2 py-1 font-semibold text-slate-700">Priority: {{ task.priority }}</span>
                  <span class="rounded-full bg-slate-200 px-2 py-1 font-semibold text-slate-700">Status: {{ task.status }}</span>
                  <span class="rounded-full bg-slate-200 px-2 py-1 font-semibold text-slate-700">Due: {{ task.dueDate ?? 'N/A' }}</span>
                  <span class="rounded-full bg-slate-200 px-2 py-1 font-semibold text-slate-700">Assigned: {{ task.assignedUsername ?? 'Unassigned' }}</span>
                </div>
              </div>

              <div v-if="canReassignTask" class="flex flex-col gap-2 sm:flex-row sm:items-center">
                <select
                  v-model.number="selectedTaskAssignees[task.id]"
                  class="w-full rounded-md border border-slate-300 bg-white px-2 py-1 text-sm text-slate-700 outline-none focus:border-blue-700 sm:w-44">
                  <option :value="null" disabled>Select member</option>
                  <option v-for="member in data.members" :key="member.userId" :value="member.userId">
                    {{ member.username }}
                  </option>
                </select>

                <button
                  type="button"
                  class="rounded-md border-2 bg-blue-300 px-3 py-1 text-sm font-medium text-blue-900 transition hover:bg-blue-900 hover:text-blue-100 disabled:opacity-60 sm:w-auto"
                  :disabled="isTaskAssignPending"
                  @click="assignTask(task.id)">
                  Assign
                </button>
              </div>
            </div>
          </li>
        </ul>
      </div>
      <div v-if="canManageProject" class="rounded-md border border-slate-200 bg-white p-5 shadow-sm">
        <div class="flex items-center justify-between gap-4">
          <h2 class="text-xl font-black text-slate-800">Project members</h2>
          <span class="rounded-full bg-slate-100 px-3 py-1 text-sm font-semibold text-slate-600">{{ data.projectMembers.length }}</span>
        </div>

        <ul class="mt-4 space-y-2">
          <li
            v-for="member in data.projectMembers"
            :key="member.userId"
            class="rounded-md border border-slate-100 bg-slate-50 px-3 py-3">
            <div class="flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
              <div class="min-w-0">
                <p class="truncate font-semibold text-slate-800">
                  {{ member.username }}
                  <span v-if="member.userId === currentUserId" class="ml-2 text-xs font-medium text-slate-500">you</span>
                </p>
              </div>

              <div class="flex flex-wrap items-center gap-2 sm:justify-end">
                <template v-if="canManageProject">
                  <div class="flex flex-nowrap items-center gap-2">
                    <select
                      v-model="selectedProjectMemberRoles[member.userId]"
                      class="w-auto max-w-24 flex-none rounded-md border border-slate-300 bg-white px-1 py-1 text-xs text-slate-700 outline-none focus:border-blue-700">
                      <option value="MEMBER">Member</option>
                      <option value="LEAD">Lead</option>
                    </select>

                    <button
                      type="button"
                      class="shrink-0 rounded-md border-2 bg-blue-300 px-2 py-1 text-xs font-medium text-blue-900 transition hover:bg-blue-900 hover:text-blue-100 disabled:opacity-60"
                      :disabled="isRoleMutationPending"
                      @click="addMemberToTeam(member.userId)">
                      Add
                    </button>
                  </div>
                </template>
              </div>
            </div>
          </li>
        </ul>
      </div>
    </template>
  </section>
</template>

<script setup lang="ts">
import BackButton from '@/shared/components/BackButton.vue'
import InviteUserButton from '@/features/notifications/components/InviteUserButton.vue'
import { useGetTeamDetailsQuery, useUpdateTeamMemberRoleMutation } from '../queries/team.queries'
import type { TeamRole, TeamMemberOverview } from '../DTOs/team.dtos'
import type { ProjectMemberOverview } from '@/features/projects/DTOs/project.dtos'
import { computed, reactive, watchEffect } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { AlertDialog, AlertDialogTrigger, AlertDialogContent, AlertDialogHeader, AlertDialogDescription, AlertDialogFooter, AlertDialogCancel, AlertDialogAction } from '@/components/ui/alert-dialog'
import { Trash2Icon } from '@lucide/vue'
import { useDeleteTeamMutation } from '../queries/team.queries'
import { useGetProjectQuery } from '@/features/projects/queries/project.queries'
import { useGetProjectTasksQuery, useUpdateTaskAssigneeMutation } from '@/features/tasks/queries/task.queries'
import type { TaskResponse } from '@/features/tasks/DTOs/task.dtos'
import { useUserProfileQuery } from '@/features/users/queries/users.querys'
import { saveKanbanContext } from '@/features/kanban/composables/kanbanContext'
import { toast } from 'vue-sonner'

const route = useRoute()

const projectId = computed(() => Number(route.params.projectId))
const teamId = computed(() => Number(route.params.teamId))

const { data, isLoading, isError, error } = useGetTeamDetailsQuery(projectId.value, teamId.value)
const { data: projectData } = useGetProjectQuery(projectId.value)
const { data: profile } = useUserProfileQuery()

const roleMutation = useUpdateTeamMemberRoleMutation()
const deleteTeamMutation = useDeleteTeamMutation()
const taskAssignMutation = useUpdateTaskAssigneeMutation()
const router = useRouter()
const { data: allTeamTasks } = useGetProjectTasksQuery(projectId.value, teamId.value)

const currentUserId = computed(() => profile.value?.id)
const currentProjectMember = computed(() => projectData.value?.members.find((m: ProjectMemberOverview) => m.userId === currentUserId.value))
const canManageProject = computed(() => currentProjectMember.value?.role === 'OWNER' || currentProjectMember.value?.role === 'ADMIN')
const currentTeamMember = computed(() => data.value?.members.find(member => member.userId === currentUserId.value))
const canInviteToTeam = computed(() => canManageProject.value || currentTeamMember.value?.role === 'LEAD')
const canReassignTask = computed(() => canManageProject.value || currentTeamMember.value?.role === 'LEAD')
const canViewTeamKanban = computed(() => canManageProject.value || currentTeamMember.value?.role === 'LEAD')
const isRoleMutationPending = computed(() => roleMutation.isPending.value)
const isTaskAssignPending = computed(() => taskAssignMutation.isPending.value)
const teamTasks = computed(() => (allTeamTasks.value ?? []).filter(task => task.assignedUserId === null))

const selectedProjectMemberRoles = reactive<Record<number, TeamRole>>({})
const selectedMemberRoles = reactive<Record<number, TeamRole>>({})
const selectedTaskAssignees = reactive<Record<number, number | null>>({})

watchEffect(() => {
  if (!data.value) return

  for (const member of data.value.projectMembers) {
    if (selectedProjectMemberRoles[member.userId] === undefined) {
      selectedProjectMemberRoles[member.userId] = 'MEMBER'
    }
  }

  for (const member of data.value.members) {
    if (selectedMemberRoles[member.userId] === undefined) {
      selectedMemberRoles[member.userId] = member.role || 'MEMBER'
    }
  }

  for (const task of (allTeamTasks.value ?? []) as TaskResponse[]) {
    if (selectedTaskAssignees[task.id] === undefined) {
      selectedTaskAssignees[task.id] = task.assignedUserId ?? null
    }
  }
})

function canViewMemberKanban(memberUserId: number) {
  if (memberUserId === currentUserId.value) return true

  return canViewTeamKanban.value
}

function goToTeamKanban() {
  saveKanbanContext({
    projectId: projectId.value,
    teamId: teamId.value,
  })

  router.push({
    name: 'project-kanban-view',
    query: {
      projectId: projectId.value,
      teamId: teamId.value,
    },
  })
}

function goToMemberKanban(memberUserId: number) {
  if (memberUserId === currentUserId.value) {
    saveKanbanContext({
      projectId: projectId.value,
      teamId: teamId.value,
      userId: currentUserId.value,
    })

    router.push({
      name: 'project-kanban-view',
      query: {
        projectId: projectId.value,
        teamId: teamId.value,
        userId: currentUserId.value,
      },
    })
    return
  }

  saveKanbanContext({
    projectId: projectId.value,
    teamId: teamId.value,
    userId: memberUserId,
  })

  router.push({
    name: 'project-kanban-view',
    query: {
      projectId: projectId.value,
      teamId: teamId.value,
      userId: memberUserId,
    },
  })
}

function memberBadgeClass(role: TeamRole) {
  switch (role) {
    case 'LEAD':
      return 'bg-amber-100 text-amber-800'
    default:
      return 'bg-slate-100 text-slate-700'
  }
}

async function addMemberToTeam(userId: number) {
  const role = selectedProjectMemberRoles[userId]
  if (!role) return

  try {
    await roleMutation.mutateAsync({ projectId: projectId.value, teamId: teamId.value, userId, role })

    toast.success('Member added to team')
  } catch (mutationError) {
    toast.error(mutationError instanceof Error ? mutationError.message : 'Failed to add member to team')
  }
}

async function updateTeamMemberRole(userId: number) {
  const role = selectedMemberRoles[userId]
  if (!role) return

  const currentRole = data.value?.members.find((m: TeamMemberOverview) => m.userId === userId)?.role
  if (currentRole === role) {
    toast.error('No changes to save')
    return
  }

  try {
    await roleMutation.mutateAsync({ projectId: projectId.value, teamId: teamId.value, userId, role })
    toast.success('Team member role updated')
  } catch (mutationError) {
    toast.error(mutationError instanceof Error ? mutationError.message : 'Failed to update team member role')
  }
}

async function assignTask(taskId: number) {
  const assigneeId = selectedTaskAssignees[taskId]
  if (!assigneeId) {
    toast.error('Select a member first')
    return
  }

  try {
    await taskAssignMutation.mutateAsync({
      projectId: projectId.value,
      taskId,
      payload: { assignedUserId: assigneeId },
    })
    toast.success('Task assigned successfully')
    router.push({
      name: 'project-kanban-view',
      query: {
        projectId: projectId.value,
        teamId: teamId.value,
        userId: assigneeId,
      },
    })
  } catch (error) {
    toast.error(error instanceof Error ? error.message : 'Failed to assign task')
  }
}

function onDeleteTeam() {
  if (deleteTeamMutation.isPending.value) return

  deleteTeamMutation.mutate({ projectId: projectId.value, teamId: teamId.value }, {
    onSuccess: () => {
      toast.success('Team deleted successfully')
      router.push({ name: 'project-details', params: { projectId: projectId.value } })
    },
    onError: (err) => toast.error(err instanceof Error ? err.message : 'Failed to delete team')
  })
}
</script>
