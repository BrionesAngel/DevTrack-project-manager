<script setup lang="ts">
import { RouterView } from 'vue-router'
import NavBar from './shared/components/NavBar.vue';
</script>

<template>
  <div class="flex flex-col min-h-screen bg-gray-200">
    <NavBar class="shrink-0"></NavBar>
    <main class="flex-1 h-full overflow-auto">
      <RouterView />
    </main>
  </div>
</template>

<style scoped></style>
