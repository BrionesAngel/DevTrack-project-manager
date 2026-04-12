<template>
  <header class="font-medium">
    <div class="flex flex-row bg-white shadow-md shadow-slate-500">
      <div class="flex flex-row">
        <button @click="open = true" class="text-3xl p-2 md:hidden bg-lime-300">☰</button>
        <div class="flex flex-row gap-4 justify-start items-center">
          <img class=" h-14 " src="@/assets/devtrack_logo.png" />
        </div>
      </div>

      <!-- mobile -->
      <Sidebar :open="open" @close="open = false">
        <div class="md:hidden">
          <div class="flex flex-row gap-4 p-1 justify-start items-center">
            <img class="h-18 " src="@/assets/devtrack_logo.png" />
          </div>
          <nav class="flex flex-col gap-4 pt-4">
            <RouterLink class="py-4 pl-2" v-for="link in links" :key="link.to" :to="link.to" @click="open = false"
              :class="{ 'bg-lime-50 border-l-8 border-lime-300': isActive(link.to) }">{{
                link.name }}
            </RouterLink>
          </nav>
        </div>
      </Sidebar>

      <!-- desktop -->
      <nav class="hidden md:flex flex-row">
        <RouterLink class="py-3 px-10 hover:bg-lime-50" v-for="link in links" :key="link.to" :to="link.to"
          @click="open = false" :class="{ 'bg-lime-50 border-b-6 border-lime-300': isActive(link.to) }">
          {{ link.name }}
        </RouterLink>
      </nav>
    </div>
  </header>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useLinks } from '@/composables/useLinks'
import Sidebar from './SideBar.vue';
import { useRoute } from 'vue-router';
const { links } = useLinks()
const open = ref(false)

const route = useRoute()
const isActive = (to: string) => {
  if (to === '/') return route.path === '/'
  return route.path.startsWith(to)
}
</script>

<style scoped></style>
