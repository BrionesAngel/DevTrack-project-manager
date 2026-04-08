<template>
  <div class="flex flex-col justify-start gap-16 items-center min-h-screen">
    <div class="flex flex-col justify-center items-center w-full gap-8 mt-16">
      <h1 class="text-4xl">DashBoard Overview</h1>
      <ul class="flex flex-row justify-around w-full">
        <li>
          <h3 class="text-2xl">name project</h3>
          <p>description project</p>
        </li>

        <li>
          <h3 class="text-2xl">name project</h3>
          <p>description project</p>
        </li>

        <li>
          <h3 class="text-2xl">name project</h3>
          <p>description project</p>
        </li>

      </ul>
    </div>

    <div class="flex flex-col justify-center items-center w-full gap-8 mt-16">
      <h1 class="text-4xl">Recent Projects</h1>
      <ul class="flex flex-row flex-wrap justify-around w-full">
        <li v-for="i in 4" :key="i">
          <ProjectCard></ProjectCard>
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup lang="ts">
import ProjectCard from '../components/ProjectCard.vue';


</script>

<style scoped></style>
