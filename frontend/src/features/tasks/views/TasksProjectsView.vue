<template>
  <div class="min-h-screen pb-8">
    <div class="flex justify-between items-center mx-4 my-4">
      <span class="text-4xl">Tasks</span>
      <CreateTaskButton></CreateTaskButton>
    </div>
    <ul class="w-full md:grid md:grid-cols-2 lg:grid-cols-3">
      <li v-for="i in 7" :key="i">
        <div class="m-4">
          <TasksProjectsCard :project-id="i"></TasksProjectsCard>
        </div>
      </li>
    </ul>
  </div>
</template>

<script setup lang="ts">
import CreateTaskButton from '../components/CreateTaskButton.vue';
import TasksProjectsCard from '../components/TasksProjectsCard.vue';
</script>
