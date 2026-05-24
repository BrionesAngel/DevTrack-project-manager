<template>
  <div class="flex flex-1 flex-col justify-start gap-1 md:gap-8 items-center ">
    <div class="flex flex-col justify-center items-center w-full gap-6 mt-4">
      <h1 class="text-4xl">DashBoard Overview</h1>
      <div class="flex flex-col md:flex-row flex-wrap justify-center md:justify-between w-full ">
        <DelayedTasksCard></DelayedTasksCard>
        <RejectedTasksCard></RejectedTasksCard>
        <PendindgTasksCard></PendindgTasksCard>
        <InprogressTasksCard></InprogressTasksCard>
      </div>
    </div>

    <div class="flex flex-col justify-center items-center w-full gap-8 mt-4">
      <div class="flex flex-row justify-center items-center px-4 w-full">
        <h1 class="text-4xl">Recent Projects</h1>
      </div>
      <ul class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 md:flex-row justify-center w-full">
        <li v-for="project in data?.slice(-3).reverse()" :key="project.id">
          <ProjectCard class="m-4" :with-tasks-shortcut="true" :project="project"></ProjectCard>
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup lang="ts">import DelayedTasksCard from '@/shared/components/DelayedTasksCard.vue';
import InprogressTasksCard from '@/shared/components/InprogressTasksCard.vue';

import PendindgTasksCard from '@/shared/components/PendindgTasksCard.vue';
import ProjectCard from '@/shared/components/ProjectCard.vue';
import RejectedTasksCard from '@/shared/components/RejectedTasksCard.vue';
import { useGetAllProjectsQuery } from '../queries/project.queries';


const {data, isLoading, isError, error} = useGetAllProjectsQuery()

</script>

<style scoped></style>
