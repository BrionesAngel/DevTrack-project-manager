<template>
  <section class="flex mx-auto h-full w-full  items-center justify-center px-4" style="min-height: calc(100vh - 64px)">
    <div class="w-full max-w-md rounded-2xl border border-slate-200 bg-white p-8 shadow-lg mb-16">
      <h1 class="text-2xl font-bold text-slate-900">Login</h1>
      <p class="mt-1 text-sm text-slate-600">Sign in to manage your app.</p>

      <form class="mt-6 space-y-4" @submit.prevent="onSubmit">
        <div>
          <label class="mb-1 block text-sm font-medium text-slate-700" for="email">Email</label>
          <input id="email" v-model="form.email" type="email" required autocomplete="email"
            class="w-full rounded-lg border border-slate-300 px-3 py-2 outline-none ring-indigo-400 focus:ring" />
        </div>

        <div>
          <label class="mb-1 block text-sm font-medium text-slate-700" for="password">Password</label>
          <input id="password" v-model="form.password" type="password" required autocomplete="current-password"
            class="w-full rounded-lg border border-slate-300 px-3 py-2 outline-none ring-indigo-400 focus:ring" />
        </div>

        <p v-if="error" class="text-sm text-rose-600">{{ error }}</p>

        <button type="submit" class="w-full rounded-lg bg-indigo-900 px-4 py-2 font-medium text-white hover:bg-indigo-950 cursor-pointer disabled:cursor-not-allowed disabled:opacity-60
" :disabled="authStore.isLoading">
          {{ authStore.isLoading ? 'Signing in...' : 'Sign in' }}
        </button>
        <p class="text-center text-sm text-slate-500">
          Don't have an account?
          <RouterLink to="/register" class="font-medium text-indigo-900 hover:underline">
            Register
          </RouterLink>
        </p>
      </form>
    </div>
  </section>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth.store'
import { HttpError } from '@/api/axios'

const authStore = useAuthStore()
const router = useRouter()

const form = reactive({
  email: '',
  password: '',
})

const error = ref('')

async function onSubmit() {
  if (authStore.isLoading) return
  error.value = ''

  try {
    if (!validate()) return
    await authStore.login(form)
    await router.push({ name: 'home-dashboard' })
    form.email = ''
    form.password = ''
  } catch (err) {
    error.value = err instanceof HttpError ? err.message : 'Login failed. Please try again.'
  }
}
function validate() {
  if (!form.email || !form.password) {
    error.value = 'Email and password are required'
    return false
  }
  return true
}
</script>
