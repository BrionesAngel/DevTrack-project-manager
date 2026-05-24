<template>
  <div class="flex flex-1 flex-col pb-8">
    <div class="mx-4 my-4">
      <span class="text-4xl">Teams</span>
    </div>
    <ul class="w-full grid grid-cols-1 md:grid-cols-2">
      <li v-for="project in data" :key="project.id">
        <div class="m-4">
          <TeamsProjectsCard></TeamsProjectsCard>
        </div>
      </li>
    </ul>
  </div>
</template>

<script setup lang="ts">
import TeamsProjectsCard from '../components/TeamsProjectsCard.vue';


</script>
