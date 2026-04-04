<template>
  <header>
    <button @click="open = true" class="text-3xl">☰</button>

    <Sidebar :open="open" @close="open = false">
      <div class="flex flex-row gap-4 p-1 justify-start items-center">
        <img class="w-10 h-10 " src="@/assets/logo.svg" />
        <p class="pb-1">DevTrack</p>
      </div>
      <nav class="flex flex-col gap-4 pt-4">
        <RouterLink class="py-4 pl-2" v-for="link in links" :key="link.to" :to="link.to" @click="open = false">{{
          link.name }}
        </RouterLink>
      </nav>
    </Sidebar>
  </header>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useLinks } from '@/composables/useLinks'
import Sidebar from './Sidebar.vue';
const { links } = useLinks()
const open = ref(false)

</script>

<style scoped></style>
