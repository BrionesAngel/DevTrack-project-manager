<template>
  <div class="border rounded-md p-2 bg-zinc-200 hover:bg-white transition">
    <span class="text-xs text-gray-400">#42</span>
    <p class="text-sm truncate mb-2">short description</p>

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

withDefaults(defineProps<{
  showActions?: boolean
}>(), {
  showActions: false,
})
</script>
