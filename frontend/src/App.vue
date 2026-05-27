<script setup lang="ts">
import { computed } from 'vue'
import { RouterView, useRoute } from 'vue-router'
import NavBar from './shared/components/NavBar.vue';
import { Toaster } from 'vue-sonner';

const route = useRoute()
const showNavBar = computed(() => route.name !== 'login' && route.name !== 'register')
</script>

<template>
  <div class="flex flex-col min-h-screen bg-gray-200">
    <NavBar v-if="showNavBar" class="shrink-0"></NavBar>
    <main class="flex flex-1 min-h-0 overflow-auto">
      <div class="flex-1 min-w-0">
        <RouterView />
      </div>
      <Toaster rich-colors position="top-center" :duration="2000"></Toaster>
    </main>
  </div>
</template>

<style scoped></style>
