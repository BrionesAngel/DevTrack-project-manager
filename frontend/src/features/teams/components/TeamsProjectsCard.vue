<template>
  <div :class="showMembers ? 'bg-white' : 'bg-zinc-100'" class=" flex flex-col justify-center p-4 rounded-md border-0.5 border-indigo-800 shadow-sm
    shadow-indigo-950 transition-transform duration-200 hover:shadow-md hover:bg-white">
    <h3 class="text-2xl mb-2">project name</h3>
    <button @click.stop="showMembers = !showMembers"
      class="flex items-center border-2 p-1 rounded-md gap-1 text-md text-blue-600 hover:text-blue-800 transition">
      <span>{{ showMembers ? '▲' : '▼' }}</span>
      <span>members</span>
    </button>
    <transition name="slide">
      <div v-if="showMembers" class="border-t pt-3 mt-3">
        <ul class="space-y-2">
          <li>name: name 1</li>
          <li>name: name 2</li>
          <li>name: name 3</li>
        </ul>
      </div>
    </transition>
  </div>









</template>

<script setup lang="ts">
import { ref } from 'vue';

const showMembers = ref(false)
</script>
