<template>
  <div class="flex bg-zinc-100 p-4 m-4 rounded-md border-0.5 border-teal-800 shadow-sm shadow-teal-950 transition-transform duration-200
      hover:scale-105
      hover:shadow-md
      hover:shadow-blue-600
      hover:bg-white">
    <div class="flex items-center gap-2">
      <div class="p-4 bg-blue-100 rounded-lg">
        <CalendarClockIcon class="text-blue-500 size-8">
        </CalendarClockIcon>
      </div>
      <div class="text-2xl">
        <h3>in progress tasks</h3>
        <h2>8</h2>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { CalendarClockIcon } from '@lucide/vue';



</script>
