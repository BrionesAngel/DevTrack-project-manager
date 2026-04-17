<template>
  <div class="min-h-screen pb-8 m-4">
    <div class="flex items-center justify-between gap-2 mb-4">
      <div class="flex items-center gap-2">
        <BackButton fallback="tasks"></BackButton>
        <h2 class="text-3xl">Project {{ projectId }} Tasks</h2>
      </div>
      <CreateTaskButton :project-id="projectId"></CreateTaskButton>
    </div>

    <ul class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-3">
      <li v-for="i in 10" :key="i">
        <TaskCard :show-actions="true"></TaskCard>
      </li>
    </ul>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { useRoute } from 'vue-router';
import BackButton from '@/shared/components/BackButton.vue';
import CreateTaskButton from '../components/CreateTaskButton.vue';
import TaskCard from '../components/TaskCard.vue';

const route = useRoute()
const projectId = computed(() => route.params.projectId as string)
</script>
