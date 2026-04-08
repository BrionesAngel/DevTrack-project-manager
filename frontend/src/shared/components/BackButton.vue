<template>
  <button @click="goBack()">
    <div class="rounded-full p-2 bg-lime-50">
      <CircleArrowLeftIcon class="text-lime-300"></CircleArrowLeftIcon>
    </div>
  </button>
</template>

<script setup lang="ts">
import { CircleArrowLeftIcon } from '@lucide/vue';
import { useRouter } from 'vue-router';
const props = defineProps<{
  fallback: string
}>()
const router = useRouter()

function goBack() {
  if (window.history.length > 1) {
    router.back()
  } else {
    router.push({ name: props.fallback })
  }
}
</script>
