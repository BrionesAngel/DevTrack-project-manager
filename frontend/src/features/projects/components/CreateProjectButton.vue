<template>
  <div
    class="p-2 pd-2 rounded-lg bg-indigo-600 shadow-md hover:bg-indigo-800 hover:scale-105 transition-transform duration-200 ">
    <button @click="onCreateProject" class="flex flex-row justify-center items-center gap-2">
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
