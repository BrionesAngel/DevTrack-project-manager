<template>
  <section class="mx-auto flex w-full max-w-3xl flex-col gap-6 px-4 py-8">
    <div>
      <p class="text-sm font-medium uppercase tracking-[0.2em] text-slate-500">Profile</p>
      <h1 class="mt-2 text-3xl font-semibold text-slate-900">Your account</h1>
    </div>

    <div class="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm">
      <p v-if="isLoading" class="text-sm text-slate-500">Loading profile...</p>

      <p v-else-if="isError" class="text-sm text-rose-600">
        {{ errorMessage }}
      </p>

      <div v-else-if="profile" class="space-y-4">
        <div class="rounded-xl border border-slate-200 bg-slate-50 p-4">
          <div class="flex items-center justify-between gap-3">
            <div>
              <p class="text-xs font-medium uppercase tracking-[0.2em] text-slate-500">Username</p>
              <p v-if="!isEditingUsername" class="mt-2 text-lg font-semibold text-slate-900">
                {{ profile.userName }}
              </p>
            </div>

            <button
              type="button"
              class="inline-flex items-center gap-2 rounded-lg border border-slate-300 bg-white px-3 py-2 text-sm font-medium text-slate-700 hover:bg-slate-100"
              @click="startUsernameEdit"
            >
              <FilePenLineIcon class="h-4 w-4" />
              Edit
            </button>
          </div>

          <form v-if="isEditingUsername" class="mt-4 flex flex-col gap-3 sm:flex-row" @submit.prevent="submitUsername">
            <input
              v-model="usernameForm.username"
              type="text"
              class="w-full rounded-lg border border-slate-300 px-3 py-2 outline-none focus:ring-2 focus:ring-slate-300"
              placeholder="New username"
              maxlength="20"
            />
            <div class="flex gap-2 sm:shrink-0">
              <button
                type="button"
                class="rounded-lg border border-slate-300 px-3 py-2 text-sm font-medium text-slate-700 hover:bg-slate-100"
                @click="cancelUsernameEdit"
              >
                Cancel
              </button>
              <button
                type="submit"
                class="rounded-lg bg-slate-900 px-3 py-2 text-sm font-medium text-white hover:bg-slate-800 disabled:cursor-not-allowed disabled:opacity-60"
                :disabled="usernameMutation.isPending.value"
              >
                {{ usernameMutation.isPending.value ? 'Saving...' : 'Save' }}
              </button>
            </div>
          </form>

          <p v-if="usernameMessage" class="mt-3 text-sm text-rose-600">{{ usernameMessage }}</p>
        </div>

        <div class="rounded-xl border border-slate-200 bg-slate-50 p-4">
          <div class="flex items-center justify-between gap-3">
            <div>
              <p class="text-xs font-medium uppercase tracking-[0.2em] text-slate-500">Password</p>
              <p class="mt-2 text-lg font-semibold text-slate-900">********</p>
            </div>

            <button
              type="button"
              class="inline-flex items-center gap-2 rounded-lg border border-slate-300 bg-white px-3 py-2 text-sm font-medium text-slate-700 hover:bg-slate-100"
              @click="startPasswordEdit"
            >
              <KeyRoundIcon class="h-4 w-4" />
              Change
            </button>
          </div>

          <form v-if="isChangingPassword" class="mt-4 grid gap-3" @submit.prevent="submitPassword">
            <input
              v-model="passwordForm.currentPassword"
              type="password"
              class="w-full rounded-lg border border-slate-300 px-3 py-2 outline-none focus:ring-2 focus:ring-slate-300"
              placeholder="Current password"
            />
            <input
              v-model="passwordForm.newPassword"
              type="password"
              class="w-full rounded-lg border border-slate-300 px-3 py-2 outline-none focus:ring-2 focus:ring-slate-300"
              placeholder="New password"
            />
            <input
              v-model="passwordForm.confirmNewPassword"
              type="password"
              class="w-full rounded-lg border border-slate-300 px-3 py-2 outline-none focus:ring-2 focus:ring-slate-300"
              placeholder="Confirm new password"
            />

            <div class="flex gap-2">
              <button
                type="button"
                class="rounded-lg border border-slate-300 px-3 py-2 text-sm font-medium text-slate-700 hover:bg-slate-100"
                @click="cancelPasswordEdit"
              >
                Cancel
              </button>
              <button
                type="submit"
                class="rounded-lg bg-slate-900 px-3 py-2 text-sm font-medium text-white hover:bg-slate-800 disabled:cursor-not-allowed disabled:opacity-60"
                :disabled="passwordMutation.isPending.value"
              >
                {{ passwordMutation.isPending.value ? 'Updating...' : 'Update password' }}
              </button>
            </div>
          </form>

          <p v-if="passwordMessage" class="mt-3 text-sm text-rose-600">{{ passwordMessage }}</p>
        </div>

        <div class="rounded-xl bg-slate-50 p-4">
          <p class="text-xs font-medium uppercase tracking-[0.2em] text-slate-500">Email</p>
          <p class="mt-2 text-lg font-semibold text-slate-900">{{ profile.email }}</p>
        </div>
      </div>

      <p v-else class="text-sm text-slate-500">No profile data available.</p>
    </div>
  </section>
</template>

<script setup lang="ts">
import { computed, reactive, ref } from 'vue'
import { FilePenLineIcon, KeyRoundIcon } from '@lucide/vue'
import { HttpError } from '@/api/axios'
import { useUserProfileQuery } from '../services/users.querys'
import { useChangePasswordMutation, useUpdateUsernameMutation } from '../services/users.querys'

const { data, isLoading, isError, error } = useUserProfileQuery()
const usernameMutation = useUpdateUsernameMutation()
const passwordMutation = useChangePasswordMutation()

const profile = computed(() => data.value ?? null)
const errorMessage = computed(() =>
  error.value instanceof Error ? error.value.message : 'Unable to load the profile.'
)

const isEditingUsername = ref(false)
const isChangingPassword = ref(false)

const usernameForm = reactive({ username: '' })
const passwordForm = reactive({
  currentPassword: '',
  newPassword: '',
  confirmNewPassword: '',
})

const usernameMessage = ref('')
const passwordMessage = ref('')

function startUsernameEdit() {
  usernameMessage.value = ''
  usernameForm.username = profile.value?.userName ?? ''
  isEditingUsername.value = true
}

function cancelUsernameEdit() {
  isEditingUsername.value = false
  usernameMessage.value = ''
}

function startPasswordEdit() {
  passwordMessage.value = ''
  passwordForm.currentPassword = ''
  passwordForm.newPassword = ''
  passwordForm.confirmNewPassword = ''
  isChangingPassword.value = true
}

function cancelPasswordEdit() {
  isChangingPassword.value = false
  passwordMessage.value = ''
}

async function submitUsername() {
  usernameMessage.value = ''

  const nextUsername = usernameForm.username.trim()
  if (nextUsername.length < 2 || nextUsername.length > 20) {
    usernameMessage.value = 'Username must be between 2 and 20 characters.'
    return
  }

  try {
    await usernameMutation.mutateAsync({ username: nextUsername })
    isEditingUsername.value = false
  } catch (err) {
    usernameMessage.value = err instanceof HttpError ? err.message : 'Unable to update the username.'
  }
}

async function submitPassword() {
  passwordMessage.value = ''

  if (!passwordForm.currentPassword || !passwordForm.newPassword || !passwordForm.confirmNewPassword) {
    passwordMessage.value = 'Fill all password fields.'
    return
  }

  if (passwordForm.newPassword !== passwordForm.confirmNewPassword) {
    passwordMessage.value = 'New passwords do not match.'
    return
  }

  try {
    await passwordMutation.mutateAsync({ ...passwordForm })
    isChangingPassword.value = false
  } catch (err) {
    passwordMessage.value = err instanceof HttpError ? err.message : 'Unable to update the password.'
  }
}

</script>
