<template>
  <div>
    <ul class="w-full">
      <li v-for="i in 5" :key="i">
        <div class="m-12">
          <ProjectCard></ProjectCard>
        </div>
      </li>
    </ul>
  </div>
</template>

<script setup lang="ts">
import ProjectCard from '@/shared/components/ProjectCard.vue';


</script>
