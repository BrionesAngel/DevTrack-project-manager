<template>
  <div class="mx-auto flex w-full max-w-6xl flex-col gap-6 p-4">
    <div class="flex items-center gap-3">
      <BackButton fallback="projects" />
      <div>
        <p class="text-sm font-semibold uppercase tracking-[0.2em] text-slate-500">Project details</p>
        <h1 class="text-4xl font-black text-slate-800">{{ data?.title }}</h1>
      </div>
    </div>

    <div v-if="isLoading" class="rounded-md border border-slate-200 bg-white p-6 text-slate-500">
      Loading project...
    </div>

    <div v-else-if="isError" class="rounded-md border border-rose-200 bg-rose-50 p-6 text-rose-700">
      Failed to load project.
      {{ error?.message }}
    </div>

    <template v-else-if="data">
      <section class="grid gap-4 lg:grid-cols-[minmax(0,1.55fr)_minmax(320px,0.85fr)] lg:items-start">
        <div class="flex min-w-0 flex-col gap-4">
          <div class="rounded-md border border-slate-200 bg-white p-5 shadow-sm">
            <p class="text-sm font-semibold uppercase tracking-[0.15em] text-slate-500">Overview</p>
            <p class="mt-3 text-lg text-slate-600">
              {{ data.description || 'This project does not have a description yet.' }}
            </p>

            <div class="mt-5 flex flex-wrap gap-3">
              <template v-if="canManageProject">
                <CreateTeamButton :projectId="data.id" />
                <CreateTaskButton :projectId="data.id" />
                <InviteUserButton
                  button-label="invite to project"
                  success-message="Invitation sent to the project"
                  notification-type="PROJECT_INVITATION"
                  resource-type="PROJECT"
                  :resource-id="data.id" />

              </template>
            </div>

            <p v-if="!canManageProject" class="mt-3 text-sm text-amber-600">
              Only project owners and admins can manage teams and invitations.
            </p>
          </div>

          <div class="rounded-md border border-slate-200 bg-white p-5 shadow-sm">
            <div class="flex items-center justify-between gap-4">
              <h2 class="text-xl font-black text-slate-800">Teams</h2>
              <span class="rounded-full bg-slate-100 px-3 py-1 text-sm font-semibold text-slate-600">{{ data.teams.length }}</span>
            </div>

            <ul class="mt-4 space-y-3">
              <li v-for="team in data.teams" :key="team.id" class="rounded-md border border-slate-100 bg-slate-50 p-4">
                <div class="flex flex-col gap-3 lg:flex-row lg:items-center lg:justify-between">
                  <div>
                    <p class="text-lg font-bold text-slate-800">{{ team.name }}</p>
                    <p class="text-sm text-slate-500">{{ team.members }} members</p>
                  </div>

                  <div class="flex flex-wrap gap-2">
                    <button
                      v-if="canViewTeams"
                      type="button"
                      class="flex flex-row items-center gap-2 rounded-md border-2 bg-amber-300 px-4 py-1 text-lg font-medium text-amber-900 transition hover:bg-amber-900 hover:text-amber-100 cursor-pointer"
                      @click="onTeamDetails(team.id)">
                      <InfoIcon class="size-5" />
                      <span>team details</span>
                    </button>

                  </div>
                </div>
              </li>
            </ul>
          </div>
        </div>

        <div class="rounded-md border border-slate-200 bg-white p-5 shadow-sm lg:sticky lg:top-4">
          <div class="flex items-center justify-between gap-4">
            <h2 class="text-xl font-black text-slate-800">Members</h2>
            <span class="rounded-full bg-slate-100 px-3 py-1 text-sm font-semibold text-slate-600">{{ data.members.length }}</span>
          </div>

          <ul class="mt-4 max-h-144 space-y-2 overflow-y-auto pr-1">
            <li v-for="member in data.members" :key="member.userId" class="rounded-md border border-slate-100 bg-slate-50 px-3 py-3">
              <div class="flex items-center gap-2">
                <div class="min-w-0 flex-1">
                  <p class="truncate font-semibold text-slate-800">
                    {{ member.username }}
                    <span v-if="member.userId === currentUserId" class="ml-2 text-xs font-medium text-slate-500">you</span>
                  </p>
                </div>

                <span class="shrink-0 rounded-full px-3 py-1 text-xs font-semibold" :class="memberBadgeClass(member.role)">
                  {{ member.role }}
                </span>

                <template v-if="canAssignRoles && member.userId !== currentUserId">
                  <select
                    v-model="selectedMemberRoles[member.userId]"
                    class="w-24 flex-none rounded-md border border-slate-300 bg-white px-1 py-1 text-sm text-slate-700 outline-none focus:border-blue-700">
                    <option value="MEMBER">Member</option>
                    <option value="ADMIN">Admin</option>
                    <option value="OWNER">Owner</option>
                  </select>

                  <button
                    type="button"
                    class="shrink-0 rounded-md border-2 bg-slate-300 px-2 py-1 text-sm font-medium text-slate-800 transition hover:bg-slate-800 hover:text-slate-200 disabled:opacity-60"
                    :disabled="isRoleMutationPending"
                    @click="updateMemberRole(member.userId)">
                    Save
                  </button>
                </template>
              </div>
            </li>
          </ul>
        </div>
      </section>
    </template>
  </div>
</template>

<script setup lang="ts">
import BackButton from '@/shared/components/BackButton.vue'
import CreateTeamButton from '@/features/teams/components/CreateTeamButton.vue'
import CreateTaskButton from '@/features/tasks/components/CreateTaskButton.vue'
import InviteUserButton from '@/features/notifications/components/InviteUserButton.vue'
import { useGetProjectQuery } from '../queries/project.queries'
import { computed, reactive, watchEffect } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserProfileQuery } from '@/features/users/queries/users.querys'
import { useQueryClient } from '@tanstack/vue-query'
import { InfoIcon } from '@lucide/vue'
import { useDeleteTeamMutation } from '@/features/teams/queries/team.queries'
import { toast } from 'vue-sonner'
import { useUpdateProjectMemberRoleMutation } from '../queries/project.queries'
import type { ProjectRole, ProjectResponse } from '../DTOs/project.dtos'

const route = useRoute()
const router = useRouter()
const projectId = computed(() => Number(route.params.projectId))

const { data, isLoading, isError, error } = useGetProjectQuery(projectId.value)
const { data: profile } = useUserProfileQuery()
const deleteTeamMutation = useDeleteTeamMutation()
const roleMutation = useUpdateProjectMemberRoleMutation()
const queryClient = useQueryClient()

const currentUserId = computed(() => profile.value?.id)
const currentProjectMember = computed(() => data.value?.members.find(member => member.userId === currentUserId.value))
const canManageProject = computed(() => currentProjectMember.value?.role === 'OWNER' || currentProjectMember.value?.role === 'ADMIN')
const canAssignRoles = computed(() => currentProjectMember.value?.role === 'OWNER')
const canViewTeams = computed(() => !!currentProjectMember.value)
const isRoleMutationPending = computed(() => roleMutation.isPending.value)
const selectedMemberRoles = reactive<Record<number, ProjectRole>>({})

watchEffect(() => {
  if (!data.value) return

  data.value.members.forEach(member => {
    if (selectedMemberRoles[member.userId] === undefined) {
      selectedMemberRoles[member.userId] = member.role
    }
  })
})

function memberBadgeClass(role: string) {
  switch (role) {
    case 'OWNER':
      return 'bg-amber-100 text-amber-800'
    case 'ADMIN':
      return 'bg-blue-100 text-blue-800'
    default:
      return 'bg-slate-100 text-slate-700'
  }
}

function onDeleteTeam(teamId: number) {
  deleteTeamMutation.mutate(
    { projectId: projectId.value, teamId },
    {
      onSuccess: () => toast.success('Team deleted successfully'),
      onError: (mutationError) => toast.error(mutationError instanceof Error ? mutationError.message : 'Failed to delete team'),
    }
  )
}

function onTeamDetails(teamId: number) {
  router.push({ name: 'team-details', params: { projectId: projectId.value, teamId } })
}

async function updateMemberRole(userId: number) {
  const role = selectedMemberRoles[userId]
  const currentRole = data.value?.members.find(member => member.userId === userId)?.role

  if (!role || currentRole === role) return

  try {
    await roleMutation.mutateAsync({ projectId: projectId.value, userId, payload: { role } })

    queryClient.setQueryData<ProjectResponse>(['project', projectId.value], (currentProject) => {
      if (!currentProject || !Array.isArray(currentProject.members)) {
        return currentProject
      }

      return {
        ...currentProject,
        members: currentProject.members.map((member) =>
          member.userId === userId ? { ...member, role } : member
        ),
      }
    })

    toast.success('Member role updated')
  } catch (mutationError) {
    toast.error(mutationError instanceof Error ? mutationError.message : 'Failed to update member role')
  }
}
</script>
