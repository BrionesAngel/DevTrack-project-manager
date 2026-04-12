<template>
  <div class="flex flex-col justify-center items-center m-1 p-2 max-h-full pb-16">
    <div class="flex justify-center w-full border bg-white rounded-t-2xl">
      <BackButton fallback="kanban"></BackButton>
      <span class="flex flex-9 justify-center items-center text-2xl">Kanban Member/Project Kanban</span>
    </div>
    <div class="border w-full">
      <div class="flex flex-col pd-2 lg:flex-row w-full ">
        <div v-show="activeCol === 1 || isLg" class="p-2 bg-yellow-100">
          <span class="flex justify-center">tasks assigned</span>
          <div class="grid grid-cols-3 md:grid-cols-6 lg:grid-cols-3 grid-rows-8 md:grid-rows-12 lg:grid-rows-11 gap-2">
            <TaskCard></TaskCard>
            <TaskCard></TaskCard>
            <TaskCard></TaskCard>
            <TaskCard></TaskCard>
            <TaskCard></TaskCard>
          </div>
        </div>

        <div v-show="activeCol === 2 || isLg" class="p-2 bg-blue-200">
          <span class="flex justify-center">tasks in progress</span>
          <div class="grid grid-cols-3 md:grid-cols-6 lg:grid-cols-3 grid-rows-8 md:grid-rows-12 lg:grid-rows-11 gap-2">
            <TaskCard></TaskCard>
            <TaskCard></TaskCard>
            <TaskCard></TaskCard>

          </div>
        </div>

        <div v-show="activeCol === 3 || isLg" class="p-2 bg-orange-200">
          <span class="flex justify-center">tasks to review</span>
          <div class="grid grid-cols-3 md:grid-cols-6 lg:grid-cols-3 grid-rows-8 md:grid-rows-12 lg:grid-rows-11 gap-2">
            <TaskCard></TaskCard>
            <TaskCard></TaskCard>
            <TaskCard></TaskCard>

          </div>
        </div>

        <div v-show="activeCol === 4 || isLg" class="p-2 bg-red-200">
          <span class="flex justify-center">tasks rejected</span>
          <div class="grid grid-cols-3 md:grid-cols-6 lg:grid-cols-3 grid-rows-8 md:grid-rows-12 lg:grid-rows-11 gap-2">
            <TaskCard></TaskCard>
          </div>
        </div>

        <div v-show="activeCol === 5 || isLg" class="p-2 bg-green-200">
          <span class="flex justify-center">tasks completed</span>
          <div class="grid grid-cols-3 md:grid-cols-6 lg:grid-cols-3 grid-rows-8 md:grid-rows-12 lg:grid-rows-11 gap-2">
            <TaskCard></TaskCard>
            <TaskCard></TaskCard>
            <TaskCard></TaskCard>
            <TaskCard></TaskCard>
            <TaskCard></TaskCard>
            <TaskCard></TaskCard>
            <TaskCard></TaskCard>
          </div>
        </div>
      </div>
    </div>

  </div>
  <div
    class="fixed bottom-0 left-0 right-0 h-16 bg-slate-100 shadow-xl shadow-zinc-800 z-50 flex justify-evenly items-center lg:hidden gap-2 text-sm md:text-lg"
    active-class="bg-lime-200">
    <button @click="activeCol = 1">assigned</button>
    <button @click="activeCol = 2">in proggress</button>
    <button @click="activeCol = 3">review</button>
    <button @click="activeCol = 4">rejected</button>
    <button @click="activeCol = 5">completed</button>
  </div>

</template>

<script setup lang="ts">
import TaskCard from '@/features/tasks/components/TaskCard.vue';
import BackButton from '@/shared/components/BackButton.vue';
import { ref } from 'vue';
const activeCol = ref(1)

const isLg = ref(window.innerWidth >= 1024)
window.addEventListener('resize', () => {
  isLg.value = window.innerWidth >= 1024
})
</script>
