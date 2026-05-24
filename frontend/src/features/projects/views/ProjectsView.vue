<template>
  <div class="flex flex-1 flex-col pb-8 w-full">
    <div class="flex justify-between items-center mx-4 my-4">
      <span class="text-4xl font-bold text-indigo-900">Projects</span>
      <CreateProjectButton></CreateProjectButton>
    </div>
    <ul class="w-full grid grid-cols-1 md:grid-cols-2">
      <li v-for="project in data" :key="project.id">
        <div class="m-4">
          <ProjectCard :with-tasks-shortcut="true" :project="project"></ProjectCard>
        </div>
      </li>
    </ul>
  </div>
</template>

<script setup lang="ts">
import ProjectCard from '@/shared/components/ProjectCard.vue';
import CreateProjectButton from '../components/CreateProjectButton.vue';
import { useGetAllProjectsQuery } from '../queries/project.queries';

const {data, isLoading, isError, error} = useGetAllProjectsQuery()

</script>
