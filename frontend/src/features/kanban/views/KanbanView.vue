<template>
  <div>
    <ul class="w-full md:grid md:grid-cols-3">
      <li v-for="i in 5" :key="i">
        <div class="my-8 mx-2" @click="">
          <KanbanProjectsCard></KanbanProjectsCard>
        </div>
      </li>
    </ul>
  </div>
</template>

<script setup lang="ts">
import KanbanProjectsCard from '../components/KanbanProjectsCard.vue';


</script>
