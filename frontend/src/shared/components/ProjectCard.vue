<template>
  <div class="flex flex-col bg-zinc-100 p-4 rounded-md border-0.5 border-indigo-800 shadow-sm shadow-indigo-950
      hover:shadow-md
      hover:bg-white h-52">
    <div class="flex flex-col gap-y-2">
      <h3 class="text-2xl font-black truncate text-slate-700"> {{ props.project.title }}</h3>

      <div>
        <span class="font-medium text-slate-400">createdBy: </span>
        <span class="font-bold text-slate-500">{{ props.project.createdBy }}</span>
      </div>
      <div>
        <span class="font-medium text-slate-400">teams: </span>
        <span class="font-bold text-slate-500">{{ props.project.teams }}</span>
      </div>
      <div>
        <span class="font-medium text-slate-400">members: </span>
        <span class="font-bold text-slate-500">{{ props.project.members }}</span>
      </div>

      <div class="flex justify-end items-center">
        <button
          @click="onProjectDetails"
          class="flex font-medium gap-2 p-1 border-2 px-4 rounded-md text-orange-900 bg-orange-200 hover:text-orange-200 hover:bg-orange-900 cursor-pointer">
          <info-icon></info-icon>
          <span class="font-medium">project details</span>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { ProjectOverviewResponse } from '@/features/projects/DTOs/project.dtos';
import { InfoIcon } from '@lucide/vue';
import { useRouter } from 'vue-router';

const props = defineProps<{
  project: ProjectOverviewResponse
  withTasksShortcut?: boolean
}>()

const router = useRouter()

function onProjectDetails() {
  router.push({ name: 'project-details', params: { projectId: props.project.id } })
}

</script>
