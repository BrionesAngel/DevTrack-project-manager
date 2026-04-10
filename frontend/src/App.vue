<script setup lang="ts">
import { RouterView } from 'vue-router'
import NavBar from './shared/components/NavBar.vue';
</script>

<template>
  <div class="min-h-screen bg-gray-200">
    <NavBar></NavBar>
    <RouterView />
  </div>
</template>

<style scoped></style>
