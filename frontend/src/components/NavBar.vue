<template>
  <header>
    <div class="flex flex-row bg-lime-100 border-bottom-4 border-s-transparent bg-clip-padding">
      <div class="flex flex-row">
        <button @click="open = true" class="text-3xl p-2 md:hidden">☰</button>
        <div class="flex flex-row gap-4 p-1 justify-start items-center">
          <img class="w-10 h-10 " src="@/assets/logo.svg" />
          <p class="pb-0.5">DevTrack</p>
        </div>
      </div>

      <!-- mobile -->
      <Sidebar :open="open" @close="open = false">
        <div class="md:hidden">
          <div class="flex flex-row gap-4 p-1 justify-start items-center">
            <img class="w-10 h-10 " src="@/assets/logo.svg" />
            <p class="pb-1">DevTrack</p>
          </div>
          <nav class="flex flex-col gap-4 pt-4">
            <RouterLink class="py-4 pl-2" v-for="link in links" :key="link.to" :to="link.to" @click="open = false"
              active-class="border-l-4 border-green-600 text-green-600 bg-lime-100">{{
                link.name }}
            </RouterLink>
          </nav>
        </div>
      </Sidebar>

      <!-- desktop -->
      <nav class="hidden md:flex flex-row gap-4">
        <RouterLink class="py-2 px-4 hover:bg-lime-100" v-for="link in links" :key="link.to" :to="link.to"
          @click="open = false" active-class="border-b-4 border-green-600 text-green-600">{{
            link.name }}
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
