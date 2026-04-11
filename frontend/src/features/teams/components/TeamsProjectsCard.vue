<template>
  <div :class="showMembers ? 'bg-white' : 'bg-zinc-100'" class=" flex flex-col justify-center p-4 rounded-md border-0.5 border-indigo-800 shadow-sm
    shadow-indigo-950 transition-transform duration-200 hover:shadow-md hover:bg-white">
    <h3 class="text-2xl mb-2">project name</h3>
    <div class="flex flex-row gap-8 font-medium">
      <button @click.stop="showMembers = !showMembers"
        class="flex flex-6 items-center border-2 p-1 rounded-md gap-2 text-md bg-blue-500 hover:bg-blue-700 text-blue-800 hover:text-blue-400 transition">
        <arrow-up-narrow-wide-icon v-if="showMembers"></arrow-up-narrow-wide-icon>
        <arrow-down-narrow-wide-icon v-else></arrow-down-narrow-wide-icon>
        <span class="text-white">members (5)</span>
      </button>
      <button
        class="flex flex-row justify-center items-center gap-2 border-2 px-2 rounded-md text-green-900 bg-green-400 hover:text-green-400 hover:bg-green-900">
        <user-plus-icon></user-plus-icon>
        <span>add member</span>
      </button>
    </div>
    <transition name="slide">
      <div v-if="showMembers" class="border-t mt-3">
        <ul>
          <li v-for="i in 5" :key="i" class="flex justify-between items-center border-b p-1">
            name: name {{ i }}
            <button
              class="flex flex-row font-medium items-center border-2 px-2 gap-2 rounded-md bg-red-300 hover:bg-red-900 text-red-900 hover:text-red-300">
              <user-x-icon></user-x-icon>
              <span>delete member</span>
            </button>
          </li>
        </ul>
      </div>
    </transition>
  </div>

</template>

<script setup lang="ts">
import { ArrowDownNarrowWideIcon, ArrowUpNarrowWideIcon, UserPlusIcon, UserXIcon } from '@lucide/vue';
import { ref } from 'vue';

const showMembers = ref(false)
</script>
