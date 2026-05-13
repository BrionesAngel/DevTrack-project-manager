<template>
  <div ref="target" class="relative">
    <button @click="openMenu" class="cursor-pointer rounded-4xl p-6 bg-amber-200">
      <img src="" alt="">
    </button>

    <div v-show="isOpen" ref="popover" class="absolute right-0 top-full mt-4 w-48 z-50 bg-white border
        border-gray-200 rounded-xl shadow-lg flex flex-col p-4 gap-2 justify-around items-center text-lg">
      <div class="flex items-center hover:bg-gray-100 w-full p-2 px-2 gap-2 rounded-lg cursor-pointer">
        <UserIcon></UserIcon>
        <RouterLink to="">Profile</RouterLink>
      </div>
      <div class="flex items-center hover:bg-gray-100 w-full p-2 px-2 gap-2 rounded-lg cursor-pointer">
        <SettingsIcon></SettingsIcon>
        <RouterLink to="">Settings</RouterLink>
      </div>
      <button type="button" @click="onLogout"
        class="flex items-center hover:bg-gray-100 w-full p-2 px-2 gap-2 rounded-lg cursor-pointer">
        <LogOutIcon></LogOutIcon>
        Logout
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useAuthStore } from '@/features/auth/stores/auth.store';
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { onClickOutside } from '@vueuse/core';
import { LogOutIcon, SettingsIcon, UserIcon } from '@lucide/vue';


const router = useRouter();
const authStore = useAuthStore();
const isOpen = ref(false);
const target = ref<HTMLElement | null>(null);

function onLogout() {
  authStore.logout();
  router.push({ name: 'login' });
}

function closeMenu() {
  isOpen.value = false;
}

function openMenu() {
  isOpen.value = !isOpen.value;
}

onClickOutside(target, () => {
  closeMenu();
});
</script>
