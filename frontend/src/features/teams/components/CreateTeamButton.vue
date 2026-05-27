<template>
  <div class="flex justify-center items-center">
    <button @click="onCreateTeam" class="flex items-center gap-2 rounded-md border-2 px-4 py-1 text-green-900 bg-green-300 hover:text-green-300 hover:bg-green-900 cursor-pointer">
      <UsersIcon class="size-5 shrink-0" />
      <span class="text-lg font-medium">Create Team</span>
    </button>
  </div>
</template>

<script setup lang="ts">
import { UsersIcon } from '@lucide/vue';
import { useRouter } from 'vue-router';
const router = useRouter()

const props = defineProps<{
  projectId: number
}>()

function onCreateTeam(){
  router.push({name: 'team-create', params: {projectId: props.projectId}})
}
</script>
