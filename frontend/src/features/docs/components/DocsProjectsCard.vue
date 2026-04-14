<template>
  <div class="flex flex-col bg-zinc-100 p-4 rounded-md border-0.5 border-indigo-800 shadow-sm shadow-indigo-950
      hover:shadow-md
    gap-2
      hover:bg-white">
    <h3 class="text-2xl">project name</h3>
    <div class="flex flex-row justify-between items-center gap-9 md:gap-4">
      <button
        class="flex flex-row justify-center items-center font-medium gap-2 p-1 border-2 px-2 rounded-md text-blue-900 bg-blue-300 hover:text-blue-300 hover:bg-blue-900">
        <file-pen-line-icon></file-pen-line-icon>
        <span class="">generate srs</span>
      </button>
      <button
        class="flex flex-row justify-center items-center font-medium gap-2 p-1 border-2 px-2 rounded-md text-green-900 bg-green-300 hover:text-green-300 hover:bg-green-900">
        <download-icon></download-icon>
        <span class="">download srs</span>
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { DownloadIcon, FilePenLineIcon } from '@lucide/vue';


</script>
