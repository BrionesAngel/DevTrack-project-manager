<template>
  <div class="flex flex-col justify-start gap-16 items-center min-h-screen">
    <div class="flex flex-col justify-center items-center w-full gap-8 mt-16">
      <h1 class="text-4xl">DashBoard Overview</h1>
      <ul class="flex flex-col md:flex-row justify-around w-full">
        <li v-for="i in 3" :key="i">
          <PendindgTasksCard class="m-4 md:m-0"></PendindgTasksCard>
        </li>
      </ul>
    </div>

    <div class="flex flex-col justify-center items-center w-full gap-8 mt-16">
      <h1 class="text-4xl">Recent Projects</h1>
      <ul class="flex flex-col md:flex-row flex-wrap justify-around w-full">
        <li v-for="i in 4" :key="i">
          <ProjectCard class="m-4 md:m-0"></ProjectCard>
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup lang="ts">
import PendindgTasksCard from '@/shared/components/PendindgTasksCard.vue';
import ProjectCard from '@/shared/components/ProjectCard.vue';


</script>

<style scoped></style>
