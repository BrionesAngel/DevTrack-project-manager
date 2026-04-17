<template>
  <div class="p-2 pd-2 rounded-lg bg-emerald-600 shadow-sm hover:bg-emerald-800 hover:shadow-md shadow-emerald-950">
    <button @click="onCreateTask" class="flex flex-row justify-center items-center gap-2 cursor-pointer">
      <FilePlus2Icon class="size-6 text-white"></FilePlus2Icon>
      <h2 class="text-lg font-medium text-white">Create Task</h2>
    </button>
  </div>
</template>

<script setup lang="ts">
import { FilePlus2Icon } from '@lucide/vue';
import { useRouter } from 'vue-router';

const props = defineProps<{
  projectId?: number | string
}>()

const router = useRouter()

function onCreateTask() {
  if (props.projectId !== undefined && props.projectId !== null) {
    router.push({ name: 'task-create-by-project', params: { projectId: props.projectId } })
    return
  }

  router.push({ name: 'task-create' })
}
</script>
