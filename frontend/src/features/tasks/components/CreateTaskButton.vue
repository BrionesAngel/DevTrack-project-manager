<template>
  <div class="flex justify-center items-center">
    <button @click="onCreateTask" class="flex items-center gap-2 rounded-md border-2 px-4 py-1 text-cyan-900 bg-cyan-300 hover:text-cyan-300 hover:bg-cyan-900 cursor-pointer">
      <FilePlus2Icon class="size-5 shrink-0" />
      <span class="text-lg font-medium">Create Task</span>
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
