<template>
  <div class="flex bg-zinc-100 p-4 m-4 rounded-md border-0.5 border-indigo-800 shadow-sm shadow-indigo-950
      hover:shadow-md
      hover:bg-white">
    <div class="flex items-center gap-2">
      <div class="p-4 bg-rose-100 rounded-lg">
        <CalendarClockIcon class="text-rose-600 size-8">
        </CalendarClockIcon>
      </div>
      <div class="text-2xl">
        <h3>delayed</h3>
        <h2>3</h2>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { CalendarClockIcon } from '@lucide/vue';



</script>
