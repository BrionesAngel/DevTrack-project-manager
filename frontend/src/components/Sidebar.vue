<template>
  <div v-if="open" @click="$emit('close')" class="fixed inset-0 bg-black/50 z-40"></div>

  <div class="fixed top-0 left-0 h-full w-64 bg-white shadow transform transition-transform duration-300 z-50"
    :class="open ? 'translate-x-0' : '-translate-x-full'">
    <slot></slot>
  </div>
</template>

<script setup lang="ts">
defineProps<{ open: boolean }>();
defineEmits(['close'])
</script>
<style scoped></style>
