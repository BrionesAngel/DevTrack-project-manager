<template>
  <header class="font-medium">
    <div class="flex flex-row justify-between bg-white shadow-md shadow-slate-500">
      <div class="flex flex-row w-full justify-between items-center">
        <button @click="open = true" class="text-3xl p-2 lg:hidden bg-lime-300"
          v-if="authStore.isAuthenticated">☰</button>
        <div class="flex flex-row gap-4 justify-start items-center">
          <img class="h-16 w-auto object-contain" src="@/assets/devtrack_logo.png" />
          <!-- desktop -->
          <nav class="hidden lg:flex flex-row" v-show="authStore.isAuthenticated">
            <RouterLink class="py-5 px-10 hover:bg-lime-50" v-for="link in links" :key="link.to" :to="link.to"
              @click="open = false" :class="{ 'bg-lime-50 border-b-6 border-lime-300': isActive(link.to) }">
              {{ link.name }}
            </RouterLink>
          </nav>
        </div>
        <ProfilePopoverMenu v-show="authStore.isAuthenticated" class="mr-4"></ProfilePopoverMenu>
      </div>

      <!-- mobile -->
      <Sidebar :open="open" @close="open = false">
        <div class="lg:hidden flex flex-col justify-between">
          <div class="flex flex-col gap-4 p-1 justify-center items-start">
            <img class="h-16 w-auto object-contain" src="@/assets/devtrack_logo.png" />
            <nav class="flex flex-col gap-4 pt-4">
              <RouterLink class="py-4 pl-2" v-for="link in links" :key="link.to" :to="link.to" @click="open = false"
                :class="{ 'bg-lime-50 border-l-8 border-lime-300': isActive(link.to) }">{{
                  link.name }}
              </RouterLink>
            </nav>
          </div>
        </div>
      </Sidebar>
    </div>
  </header>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useLinks } from '../composables/useLinks'
import Sidebar from './SideBar.vue';
import { useRoute } from 'vue-router';
import { useAuthStore } from '../../features/auth/stores/auth.store'
import { useRouter } from 'vue-router'
import ProfilePopoverMenu from './ProfilePopoverMenu.vue';

const authStore = useAuthStore()
const router = useRouter()
const { links } = useLinks()
const open = ref(false)
const route = useRoute()

const isActive = (to: string) => {
  if (to === '/') return route.path === '/'
  return route.path.startsWith(to)
}

</script>
