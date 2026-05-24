<script setup lang="ts">
import { RouterView } from 'vue-router'
import NavBar from './shared/components/NavBar.vue';
import { Toaster } from 'vue-sonner';
</script>

<template>
  <div class="flex flex-col min-h-screen bg-gray-200">
    <NavBar class="shrink-0"></NavBar>
    <main class="flex-1 h-full overflow-auto">
      <RouterView />
      <Toaster rich-colors position="top-center" :duration="2000"></Toaster>
    </main>
  </div>
</template>

<style scoped></style>
