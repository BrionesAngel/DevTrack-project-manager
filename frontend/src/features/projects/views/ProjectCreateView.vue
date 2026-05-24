<template>
  <section class="mx-auto flex flex-col w-full max-w-5xl">
    <div class="flex flex-col gap-4 m-8 p-4 bg-white border border-indigo-200 rounded-lg shadow-md ">
      <div class="flex flex-row bg-indigo-50 rounded-lg border-indigo-200 border-2 border-dashed">
        <h2 class="flex w-full justify-center py-4 items-center text-3xl ">Create Project</h2>
      </div>
      <form class="flex flex-col gap-8 p-4" @submit.prevent="onSubmit">

        <div>
          <label class="block text-sm font-medium text-slate-800" for="title">Title</label>
          <input id="title" v-model="form.title" type="text" required
            class="w-full rounded-lg border border-slate-300 px-3 py-2 outline-none ring-indigo-400 focus:ring" />
        </div>
        <p class="text-sm text-rose-600" v-if="isError && validationErrors?.title">{{ validationErrors.title }}</p>

        <div>
          <label class="block text-sm font-medium text-slate-800" for="description">Description</label>
          <textarea id="description" v-model="form.description" rows="4"
            class="w-full rounded-lg border border-slate-300 px-3 py-2 outline-none ring-indigo-400 focus:ring" />
        </div>
        <p class="text-sm text-rose-600" v-if="isError && validationErrors?.description">{{ validationErrors.description }}
        </p>

        <div class="flex justify-end-safe gap-6 mt-4">
          <button type="button" @click="router.back"
            class="rounded-lg bg-indigo-600 px-4 py-2 font-medium text-white hover:bg-indigo-800 disabled:cursor-not-allowed disabled:opacity-60">
            Cancel
          </button>
          <button type="submit"
            class="rounded-lg bg-indigo-600 px-4 py-2 font-medium text-white hover:bg-indigo-800 disabled:cursor-not-allowed disabled:opacity-60"
            :disabled="isPending">
            {{ isPending ? 'Creating...' : 'Create Project' }}
          </button>
        </div>

      </form>
    </div>
  </section>

</template>

<script setup lang="ts">
import { computed, reactive } from 'vue';
import { useCreateProjectMutation } from '../queries/project.queries';
import { useRouter } from 'vue-router';
import { toast } from 'vue-sonner'
import type { HttpError } from '@/api/axios';

const router = useRouter()

const { mutate, isPending, isError, error } = useCreateProjectMutation()

const validationErrors = computed(() => {
  if (!error.value) return null

  const httpError = error.value as HttpError

  return httpError.data?.errors
})

const form = reactive({
  title: '',
  description: '',
})
function onSubmit() {
  if (isPending.value) return
  mutate(form, {
    onSuccess: () => {
      toast.success("Project created successfully!")
      router.back()
    },
    onError: () => {
      toast.error("Failed to create project")
    }
  })
}
</script>
