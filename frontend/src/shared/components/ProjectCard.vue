<template>
  <div class="flex flex-col bg-zinc-100 p-4 rounded-md border-0.5 border-indigo-800 shadow-sm shadow-indigo-950
      hover:shadow-md
      hover:bg-white">
    <h3 class="text-2xl">name project</h3>
    <p>description project</p>
    <h2>70% |========| ===|</h2>
    <div class="flex flex-wrap md:flex-nowrap gap-2 justify-between items-center mt-4 w-full">
      <div class="">
        <button
          class="flex flex-row justify-center items-center font-medium gap-2 p-1 border-2 px-4 rounded-md text-orange-900 bg-orange-200 hover:text-orange-200 hover:bg-orange-900 cursor-pointer">
          <info-icon></info-icon>
          <span class="">project details</span>
        </button>
      </div>
      <div class="flex flex-row justify-between items-center gap-9 md:gap-4">
        <button
          class="flex flex-row justify-center items-center font-medium gap-2 p-1 border-2 px-2 rounded-md text-green-900 bg-green-300 hover:text-green-300 hover:bg-green-900 cursor-pointer">
          <square-pen-icon></square-pen-icon>
          <span class="">edit project</span>
        </button>
        <button
          class="flex flex-row justify-center items-center font-medium gap-2 p-1 border-2 px-2 rounded-md text-red-900 bg-red-300 hover:text-red-300 hover:bg-red-900 cursor-pointer">
          <trash2-icon></trash2-icon>
          <span class="">delete project</span>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { InfoIcon, SquarePenIcon, Trash2Icon } from '@lucide/vue';


</script>
