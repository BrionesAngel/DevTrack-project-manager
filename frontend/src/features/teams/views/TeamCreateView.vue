<template>
  <section class="mx-auto flex flex-col w-full max-w-5xl">
    <div class="flex flex-col gap-4 m-8 p-4 bg-white border border-green-200 rounded-lg shadow-md ">
      <div class="flex flex-row bg-green-50 rounded-lg border-green-200 border-2 border-dashed">
        <h2 class="flex w-full justify-center py-4 items-center text-3xl text-green-900">Create Team</h2>
      </div>
      <form class="flex flex-col gap-8 p-4" @submit.prevent="onSubmit">

        <div>
          <label class="block text-sm font-medium text-green-900" for="name">name</label>
          <input id="title" v-model="form.name" type="text" required
            class="w-full rounded-lg border border-slate-300 px-3 py-2 outline-none ring-green-400 focus:ring" />
        </div>
        <p class="text-sm text-rose-600" v-if="isError && validationErrors?.name">{{ validationErrors.name }}</p>

        <div class="flex justify-end-safe gap-6 mt-4">
          <button type="button" @click="router.back"
            class="rounded-lg bg-green-600 px-4 py-2 font-medium text-white hover:bg-green-800 disabled:cursor-not-allowed disabled:opacity-60">
            Cancel
          </button>
          <button type="submit"
            class="rounded-lg bg-green-600 px-4 py-2 font-medium text-white hover:bg-green-800 disabled:cursor-not-allowed disabled:opacity-60"
            :disabled="isPending">
            {{ isPending ? 'Creating...' : 'Create Team' }}
          </button>
        </div>

      </form>
    </div>
  </section>

</template>

<script setup lang="ts">
import { computed, reactive } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { toast } from 'vue-sonner'
import type { HttpError } from '@/api/axios';
import { useCreateTeamMutation } from '../queries/team.queries';

const route = useRoute()
const router = useRouter()

const { mutate, isPending, isError, error } = useCreateTeamMutation()

const projectId = Number(route.params.projectId)

const validationErrors = computed(() => {
  if (!error.value) return null

  const httpError = error.value as HttpError

  return httpError.data?.errors
})

const form = reactive({
  name: '',
})
function onSubmit() {
  if (isPending.value) return
  mutate({ payload: form, projectId: projectId }, {
    onSuccess: () => {
      toast.success("Team created successfully!")
      router.back()
    },
    onError: () => {
      toast.error("Failed to create team")
    }
  })
}
</script>
