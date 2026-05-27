<template>
  <div @click="$emit('open', task)" class="cursor-pointer rounded-md border border-slate-200 bg-white p-2.5 shadow-sm transition hover:-translate-y-0.5 hover:shadow-md">
    <div class="flex items-center justify-between gap-2">
      <span v-if="task" class="rounded-full bg-slate-100 px-2 py-1 text-[10px] font-semibold uppercase tracking-wide text-slate-600">
        {{ task.status }}
      </span>
    </div>

    <p class="mt-2 text-sm font-semibold leading-snug text-slate-800 line-clamp-2">
      {{ task?.title ?? 'short description' }}
    </p>

    <p v-if="task?.assignedUsername" class="mt-2 text-xs text-slate-500">
      {{ task.assignedUsername }}
    </p>

    <div v-if="showActions" class="flex gap-2 justify-end">
      <button
        class="flex flex-row justify-center items-center font-medium gap-2 p-1 border-2 px-2 rounded-md text-green-900 bg-green-300 hover:text-green-300 hover:bg-green-900 cursor-pointer">
        <square-pen-icon class="size-4"></square-pen-icon>
        <span class="text-xs">edit</span>
      </button>
      <button
        class="flex flex-row justify-center items-center font-medium gap-2 p-1 border-2 px-2 rounded-md text-red-900 bg-red-300 hover:text-red-300 hover:bg-red-900 cursor-pointer">
        <trash2-icon class="size-4"></trash2-icon>
        <span class="text-xs">delete</span>
      </button>
    </div>
  </div>


</template>

<script setup lang="ts">
import { SquarePenIcon, Trash2Icon } from '@lucide/vue';
import type { TaskResponse } from '../DTOs/task.dtos'

const emit = defineEmits<{
  (e: 'open', task?: TaskResponse | null): void
}>()

withDefaults(defineProps<{
  task?: TaskResponse | null
  showActions?: boolean
}>(), {
  showActions: false,
})
</script>

