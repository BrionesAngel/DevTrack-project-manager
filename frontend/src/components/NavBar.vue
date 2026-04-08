<template>
  <header class="font-medium">
    <div class="flex flex-row bg-teal-700 shadow-md shadow-slate-500">
      <div class="flex flex-row">
        <button @click="open = true" class="text-3xl p-2 md:hidden bg-emerald-100">☰</button>
        <div class="flex flex-row gap-4 justify-start items-center pr-4 text-green-300">
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
              active-class="border-l-8 border-teal-700 bg-emerald-100">{{
                link.name }}
            </RouterLink>
          </nav>
        </div>
      </Sidebar>

      <!-- desktop -->
      <nav class="hidden md:flex flex-row">
        <RouterLink class="py-3 px-10 hover:bg-emerald-100" v-for="link in links" :key="link.to" :to="link.to"
          @click="open = false" active-class="bg-emerald-100 border-b-6 border-teal-900">
          {{ link.name }}
        </RouterLink>
      </nav>
    </div>
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
