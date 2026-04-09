<template>
  <div class="m-8 p-6 bg-indigo-50 border-0.5 border-indigo-300 rounded-lg shadow-md">
    <div class="flex flex-row ">
      <BackButton fallback="projects"></BackButton>
      <h2 class="flex-9 flex justify-center items-center text-3xl">Create Project</h2>
    </div>
    <form class="" @submit.prevent="">

      <div>
        <label class="mb-1 block text-sm font-medium text-slate-800" for="title">Title</label>
        <input id="title" v-model="title" type="text" required
          class="w-full rounded-lg border border-slate-300 px-3 py-2 outline-none ring-indigo-300 focus:ring" />
      </div>

      <div>
        <label class="mb-1 block text-sm font-medium text-slate-800" for="description">Description</label>
        <textarea id="description" v-model="description" rows="4"
          class="w-full rounded-lg border border-slate-300 px-3 py-2 outline-none ring-indigo-300 focus:ring" />
      </div>

      <div class="flex justify-between">
        <button
          class="rounded-lg bg-indigo-600 px-4 py-2 font-medium text-white hover:bg-indigo-800 disabled:cursor-not-allowed disabled:opacity-60">Cancel</button>
        <button type="submit"
          class="rounded-lg bg-indigo-600 px-4 py-2 font-medium text-white hover:bg-indigo-800 disabled:cursor-not-allowed disabled:opacity-60"
          :disabled="isSaving">
          {{ isSaving ? 'Creating...' : 'Create Project' }}
        </button>
      </div>
    </form>
  </div>
</template>

<script setup lang="ts">
import BackButton from '@/shared/components/BackButton.vue';


</script>
