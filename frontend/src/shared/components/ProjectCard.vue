<template>
  <div class="flex flex-col bg-zinc-100 p-4 rounded-md border-0.5 border-indigo-800 shadow-sm shadow-indigo-950
      hover:shadow-md
      hover:bg-white">
    <h3 class="text-2xl">name project</h3>
    <p>description project</p>
    <h2>70% |========| ===|</h2>
    <div class="flex flex-col md:flex-row gap-2 md:gap-4 justify-between mt-4 w-full">
      <div class="flex flex-col gap-2 w-full md:w-auto">
        <button v-if="withTasksShortcut" @click="onTasks"
          class="flex flex-row justify-center items-center font-medium gap-2 p-1 border-2 px-4 rounded-md text-emerald-900 bg-emerald-200 hover:text-emerald-200 hover:bg-emerald-900 cursor-pointer w-full md:w-auto">
          <sticky-note-icon></sticky-note-icon>
          <span class="">view tasks</span>
        </button>
        <button
          class="flex flex-row justify-center items-center font-medium gap-2 p-1 border-2 px-4 rounded-md text-orange-900 bg-orange-200 hover:text-orange-200 hover:bg-orange-900 cursor-pointer w-full md:w-auto">
          <info-icon></info-icon>
          <span class="">project details</span>
        </button>
      </div>

      <div class="flex flex-col gap-2 w-full md:w-auto md:items-end">
        <button
          class="flex flex-row justify-center items-center font-medium gap-2 p-1 border-2 px-4 rounded-md text-green-900 bg-green-300 hover:text-green-300 hover:bg-green-900 cursor-pointer w-full md:w-auto">
          <square-pen-icon></square-pen-icon>
          <span class="">edit project</span>
        </button>
        <button
          class="flex flex-row justify-center items-center font-medium gap-2 p-1 border-2 px-4 rounded-md text-red-900 bg-red-300 hover:text-red-300 hover:bg-red-900 cursor-pointer w-full md:w-auto">
          <trash2-icon></trash2-icon>
          <span class="">delete project</span>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { InfoIcon, SquarePenIcon, StickyNoteIcon, Trash2Icon } from '@lucide/vue';
import { useRouter } from 'vue-router';

const props = withDefaults(defineProps<{
  withTasksShortcut?: boolean
  projectId?: string | number
}>(), {
  withTasksShortcut: false,
})

const router = useRouter()

function onTasks() {
  if (props.projectId !== undefined && props.projectId !== null) {
    router.push({ name: 'tasks-by-project', params: { projectId: props.projectId } })
    return
  }

  router.push({ name: 'tasks' })
}

</script>
