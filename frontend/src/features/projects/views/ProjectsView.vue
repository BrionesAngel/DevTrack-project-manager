<template>
  <div class="mx-auto flex w-full max-w-6xl flex-col gap-6 p-4 pb-8">
    <div class="flex justify-between items-center">
      <span class="text-4xl font-bold text-indigo-900">Projects</span>
      <CreateProjectButton></CreateProjectButton>
    </div>
    <div v-if="isLoading">
      Loading projects...
    </div>

    <div v-else-if="isError">
      Failed to load projects.
      {{error?.message}}
    </div>
    <div v-else-if="data?.length === 0" class="flex flex-1 justify-center items-start pt-8 md:pt-28">
      <p class="text-6xl text-slate-300 font-medium border-slate-300 border-4 border-dashed p-8">You are not part of any
        project yet. Start
        by
        creating one.</p>
    </div>


    <ul class="w-full grid grid-cols-1 lg:grid-cols-2" v-else>
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

const { data, isLoading, isError, error } = useGetAllProjectsQuery()

</script>
