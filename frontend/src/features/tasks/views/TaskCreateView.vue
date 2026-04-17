<template>
  <div class="m-8 p-6 bg-emerald-50 border-0.5 border-emerald-300 rounded-lg shadow-md">
    <div class="flex flex-row ">
      <BackButton fallback="tasks"></BackButton>
      <h2 class="flex-9 flex justify-center items-center text-3xl">Create Task</h2>
    </div>

    <p v-if="projectId" class="mb-4 text-slate-700">Project selected: {{ projectId }}</p>

    <form @submit.prevent="">
      <div>
        <label class="mb-1 block text-sm font-medium text-slate-800" for="title">Title</label>
        <input id="title" v-model="title" type="text" required
          class="w-full rounded-lg border border-slate-300 px-3 py-2 outline-none ring-emerald-300 focus:ring" />
      </div>

      <div>
        <label class="mb-1 block text-sm font-medium text-slate-800" for="description">Description</label>
        <textarea id="description" v-model="description" rows="4"
          class="w-full rounded-lg border border-slate-300 px-3 py-2 outline-none ring-emerald-300 focus:ring" />
      </div>

      <div>
        <label class="mb-1 block text-sm font-medium text-slate-800" for="priority">Priority</label>
        <select id="priority" v-model="priority"
          class="w-full rounded-lg border border-slate-300 px-3 py-2 outline-none ring-emerald-300 focus:ring">
          <option value="low">Low</option>
          <option value="medium">Medium</option>
          <option value="high">High</option>
        </select>
      </div>

      <div class="flex justify-between mt-4">
        <button
          class="rounded-lg bg-emerald-600 px-4 py-2 font-medium text-white hover:bg-emerald-800 disabled:cursor-not-allowed disabled:opacity-60">Cancel</button>
        <button type="submit"
          class="rounded-lg bg-emerald-600 px-4 py-2 font-medium text-white hover:bg-emerald-800 disabled:cursor-not-allowed disabled:opacity-60"
          :disabled="isSaving">
          {{ isSaving ? 'Creating...' : 'Create Task' }}
        </button>
      </div>
    </form>
  </div>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue';
import { useRoute } from 'vue-router';
import BackButton from '@/shared/components/BackButton.vue';

const route = useRoute()

const projectId = computed(() => route.params.projectId as string | undefined)

const title = ref('')
const description = ref('')
const priority = ref('medium')
const isSaving = ref(false)
</script>
