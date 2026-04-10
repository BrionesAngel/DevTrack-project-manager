<template>
  <div :class="showMembers ? 'bg-white' : 'bg-zinc-100'" class=" flex flex-col justify-center p-4 rounded-md border-0.5 border-indigo-800 shadow-sm
    shadow-indigo-950 transition-transform duration-200 hover:shadow-md hover:bg-white">
    <h3 class="text-2xl mb-2">project name</h3>
    <div class="flex flex-row gap-8">
      <button @click.stop="showMembers = !showMembers"
        class="flex flex-6 items-center border-2 p-1 rounded-md eap-1 text-md bg-blue-500 hover:bg-blue-700 text-blue-800 hover:text-blue-400 transition">
        <span>{{ showMembers ? '▲' : '▼' }}</span>
        <span class="font-medium text-white">members (5)</span>
      </button>
      <button
        class="font-medium border-2 px-2 rounded-md border-green-800 bg-green-500 text-white hover:bg-green-700 hover:border-green-400">add
        member</button>
    </div>
    <transition name="slide">
      <div v-if="showMembers" class="border-t mt-3">
        <ul>
          <li v-for="i in 5" :key="i" class="flex justify-between items-center border-b p-1">
            name: name {{ i }}
            <button
              class="flex items-center font-medium border-2 px-2 rounded-md border-red-800 bg-red-500 text-white hover:bg-red-700 hover:border-red-400">delete
              member</button>
          </li>
        </ul>
      </div>
    </transition>
  </div>

</template>

<script setup lang="ts">
import { ref } from 'vue';

const showMembers = ref(false)
</script>
