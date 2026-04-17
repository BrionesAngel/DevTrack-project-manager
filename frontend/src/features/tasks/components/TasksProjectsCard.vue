<template>
  <div class="flex flex-col bg-zinc-100 p-4 rounded-md border-0.5 border-indigo-800 shadow-sm shadow-indigo-950 hover:shadow-md hover:bg-white">
    <h3 class="text-2xl">project name {{ projectId }}</h3>
    <p>project description</p>
    <div class="flex flex-wrap gap-2 items-center mt-4 w-full">
      <button @click="onViewTasks"
        class="flex flex-row justify-center items-center font-medium gap-2 p-1 border-2 px-3 rounded-md text-blue-900 bg-blue-300 hover:text-blue-300 hover:bg-blue-900 cursor-pointer">
        <SquareKanbanIcon></SquareKanbanIcon>
        <span>view tasks</span>
      </button>
      <button @click="onCreateTask"
        class="flex flex-row justify-center items-center font-medium gap-2 p-1 border-2 px-3 rounded-md text-emerald-900 bg-emerald-300 hover:text-emerald-300 hover:bg-emerald-900 cursor-pointer">
        <FilePlus2Icon></FilePlus2Icon>
        <span>create task</span>
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { FilePlus2Icon, SquareKanbanIcon } from '@lucide/vue';
import { useRouter } from 'vue-router';

const props = defineProps<{
  projectId: number | string
}>()

const router = useRouter()

function onViewTasks() {
  router.push({ name: 'tasks-by-project', params: { projectId: props.projectId } })
}

function onCreateTask() {
  router.push({ name: 'task-create-by-project', params: { projectId: props.projectId } })
}
</script>
