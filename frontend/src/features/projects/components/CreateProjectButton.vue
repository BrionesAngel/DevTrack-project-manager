<template>
  <div class="p-2 pd-2 rounded-lg bg-indigo-600 shadow-sm hover:bg-indigo-800 hover:shadow-md  shadow-indigo-950">
    <button @click="onCreateProject" class="flex flex-row justify-center items-center gap-2
      cursor-pointer">
      <FolderPlusIcon class="size-6 text-white"></FolderPlusIcon>
      <h2 class="text-lg font-medium text-white">Create Project</h2>
    </button>
  </div>
</template>

<script setup lang="ts">
import { FolderPlusIcon } from '@lucide/vue';
import { useRouter } from 'vue-router';
const router = useRouter()
function onCreateProject() {
  router.push({ name: 'project-create' })
}
</script>
