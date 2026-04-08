<template>
  <div class="flex bg-zinc-100 p-4 rounded-md border-0.5 border-teal-800 shadow-sm shadow-teal-950 transition-transform duration-200
      hover:scale-105
      hover:shadow-md
      hover:shadow-teal-600
      hover:bg-white">
    <div class="flex items-center gap-2">
      <div class="p-4 bg-yellow-50 rounded-lg">
        <ClipboardListIcon class="text-yellow-500 size-8"></ClipboardListIcon>
      </div>
      <div>
        <h3 class="text-2xl">pending tasks</h3>
        <h2>12</h2>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ClipboardListIcon } from '@lucide/vue';



</script>
