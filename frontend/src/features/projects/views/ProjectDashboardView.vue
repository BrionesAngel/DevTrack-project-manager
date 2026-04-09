<template>
  <div class="flex flex-col justify-start gap-1 md:gap-16 items-center min-h-screen">
    <div class="flex flex-col justify-center items-center w-full gap-8 mt-4 md:mt-16">
      <h1 class="text-4xl">DashBoard Overview</h1>
      <div class="flex flex-col md:flex-row flex-wrap justify-around w-full gap-4">
        <DelayedTasksCard></DelayedTasksCard>
        <PendindgTasksCard></PendindgTasksCard>
        <InprogressTasksCard></InprogressTasksCard>
      </div>
    </div>

    <div class="flex flex-col justify-center items-center w-full gap-8 mt-4 md:mt-16">
      <div class="flex flex-row justify-evenly items-center px-4 w-full">
        <h1 class="text-4xl">Recent Projects</h1>
        <CreateProjectButton></CreateProjectButton>
      </div>
      <ul class="flex flex-col md:flex-row flex-wrap justify-around w-full">
        <li v-for="i in 4" :key="i">
          <ProjectCard class="m-4"></ProjectCard>
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup lang="ts">import DelayedTasksCard from '@/shared/components/DelayedTasksCard.vue';
import InprogressTasksCard from '@/shared/components/InprogressTasksCard.vue';

import PendindgTasksCard from '@/shared/components/PendindgTasksCard.vue';
import ProjectCard from '@/shared/components/ProjectCard.vue';
import CreateProjectButton from '../components/CreateProjectButton.vue';


</script>

<style scoped></style>
