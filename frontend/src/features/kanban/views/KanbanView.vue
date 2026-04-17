<template>
  <div class="min-h-screen pb-8">
    <div class="mx-4 my-4">
      <span class="text-4xl">Kanban</span>
    </div>
    <ul class="w-full md:grid md:grid-cols-2 lg:grid-cols-3">
      <li v-for="i in 5" :key="i">
        <div class="m-4" @click="">
          <KanbanProjectsCard></KanbanProjectsCard>
        </div>
      </li>
    </ul>
  </div>
</template>

<script setup lang="ts">
import KanbanProjectsCard from '../components/KanbanProjectsCard.vue';


</script>
