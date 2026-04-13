<template>
  <div class="min-h-screen pb-8">
    <div class="flex justify-between items-center mx-4 my-4">
      <span class="text-4xl">Projects</span>
      <CreateProjectButton></CreateProjectButton>
    </div>
    <ul class="w-full">
      <li v-for="i in 7" :key="i">
        <div class="m-4">
          <ProjectCard></ProjectCard>
        </div>
      </li>
    </ul>
  </div>
</template>

<script setup lang="ts">
import ProjectCard from '@/shared/components/ProjectCard.vue';
import CreateProjectButton from '../components/CreateProjectButton.vue';


</script>
