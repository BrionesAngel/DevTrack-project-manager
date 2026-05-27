<template>
  <div :class="showMembers ? 'bg-white' : 'bg-zinc-100'" class=" flex flex-col justify-center p-4 rounded-md border-0.5 border-indigo-800 shadow-sm
    shadow-indigo-950 transition-transform duration-200 hover:shadow-md hover:bg-white gap-y-2">
    <h3 class="text-2xl font-black truncate text-slate-700"> {{ props.project.title }}</h3>
    <div>
      <span class="font-medium text-slate-400">createdBy: </span>
      <span class="font-bold text-slate-500">{{ props.project.createdBy }}</span>
    </div>

    <div class="flex flex-row gap-8 font-medium">
      <button @click.stop="showMembers = !showMembers"
        class="flex flex-6 items-center border-2 p-1 rounded-md gap-2 text-md bg-blue-300 hover:bg-blue-900 text-blue-900 hover:text-blue-300 transition cursor-pointer">
        <arrow-up-narrow-wide-icon v-if="showMembers"></arrow-up-narrow-wide-icon>
        <arrow-down-narrow-wide-icon v-else></arrow-down-narrow-wide-icon>
        <span>teams ({{ props.project.teams.length }})</span>
      </button>
      <create-team-button :projectId="project.id"></create-team-button>
      <button
        @click="onProjectDetails"
        class="flex flex-row items-center gap-2 rounded-md border-2 border-orange-700 bg-orange-200 px-4 py-1 text-md font-medium text-orange-900 transition hover:bg-orange-900 hover:text-orange-200 cursor-pointer">
        <info-icon></info-icon>
        <span>project details</span>
      </button>
    </div>
    <transition name="slide">
      <div v-if="showMembers" class="border-t mt-3 w-full">
        <ul>
          <li v-for="team in props.project.teams" :key="team.id"
            class="flex justify-between items-center border-b p-1 ">
            <div class="flex flex-1 justify-between px-4 min-w-0">
              <div class="flex flex-3 min-w-0 gap-4">
                <span class="shrink-0">name:</span>
                <span class="truncate">{{ team.name }}</span>
              </div>
              <div class="flex flex-1 gap-4">
                <span>members:</span>
                <span>{{ team.members }}</span>
              </div>
            </div>

            <AlertDialog v-if="canManageProject">
              <AlertDialogTrigger as-child>
                <button
                  class="flex flex-row font-medium items-center border-2 px-2  py-1 gap-2 rounded-md bg-red-300 hover:bg-red-900 text-red-900 hover:text-red-300 cursor-pointer">
                  <user-x-icon></user-x-icon>
                  <span>delete team</span>
                </button>
              </AlertDialogTrigger>
              <AlertDialogContent>
                <AlertDialogHeader>
                  <AlertDialogDescription class="text-xl">
                    This action cannot be undone. The team and all its members will be permanently removed.
                  </AlertDialogDescription>
                </AlertDialogHeader>
                <AlertDialogFooter>
                  <AlertDialogCancel class="text-xl">Cancel</AlertDialogCancel>
                  <AlertDialogAction class="text-xl" @click="onDeleteTeam(team.id)">
                    Delete
                  </AlertDialogAction>
                </AlertDialogFooter>
              </AlertDialogContent>
            </AlertDialog>

            <InviteUserButton
              v-if="canInviteToTeam"
              button-label="invite to team"
              success-message="Invitation sent to the team"
              notification-type="TEAM_INVITATION"
              resource-type="TEAM"
              :resource-id="team.id" />
          </li>
        </ul>
      </div>
    </transition>

    <div v-if="isError" class="text-rose-600">
      {{ error?.message }}
    </div>

  </div>

</template>

<script setup lang="ts">
import type { ProjectTeamsResponse } from '@/features/projects/DTOs/project.dtos';
import { ArrowDownNarrowWideIcon, ArrowUpNarrowWideIcon, InfoIcon, UserXIcon } from '@lucide/vue';
import { ref, computed } from 'vue';
import CreateTeamButton from './CreateTeamButton.vue';
import router from '@/router';
import {
  AlertDialog, AlertDialogTrigger, AlertDialogContent,
  AlertDialogHeader, AlertDialogDescription,
  AlertDialogFooter, AlertDialogCancel, AlertDialogAction
} from '@/components/ui/alert-dialog'
import { useDeleteTeamMutation } from '../queries/team.queries';
import { useUserProfileQuery } from '@/features/users/queries/users.querys'
import { useGetProjectQuery } from '@/features/projects/queries/project.queries'
import { toast } from 'vue-sonner';
import InviteUserButton from '@/features/notifications/components/InviteUserButton.vue';

const props = defineProps<{
  project: ProjectTeamsResponse
}>()

const { mutate, isPending, isError, error } = useDeleteTeamMutation()

const { data: profile } = useUserProfileQuery()
const { data: projectData } = useGetProjectQuery(props.project.id)

const currentUserId = computed(() => profile.value?.id)
const currentProjectMember = computed(() => projectData.value?.members.find(member => member.userId === currentUserId.value))
const canManageProject = computed(() => currentProjectMember.value?.role === 'OWNER' || currentProjectMember.value?.role === 'ADMIN')
const canInviteToTeam = computed(() => canManageProject.value)

function onProjectDetails() {
  router.push({ name: 'project-details', params: { projectId: props.project.id } })
}

function onDeleteTeam(teamId: number) {
  if (isPending.value) return
  mutate({ projectId: props.project.id, teamId: teamId }, {
    onSuccess: () => {
      toast.success("team deleted successfully!")
    },
    onError: () => {
      toast.error("Failed to delete team")
    }
  })
}

const showMembers = ref(false)
</script>
