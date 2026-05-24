<template>
  <div>
<button @click=""
  class="flex flex-row justify-center items-center font-medium gap-2 p-1 border-2 px-4 rounded-md text-emerald-900 bg-emerald-200 hover:text-emerald-200 hover:bg-emerald-900 cursor-pointer w-full md:w-auto">
  <sticky-note-icon></sticky-note-icon>
  <span class="">view tasks</span>
</button>

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
</template>

<script setup lang="ts">

</script>
