<script setup lang="ts">
import { RouterView } from 'vue-router'
import NavBar from './components/NavBar.vue';
</script>

<template>
  <NavBar class="md:hidden"></NavBar>
  <RouterView />
</template>

<style scoped>
.nav-link {
  display: inline-block;
  padding: 1rem 2rem;
  border-bottom: 4px solid transparent;
  background-clip: padding-box;
}

.nav-link:hover {
  background-color: lightgreen;
}

nav a.router-link-exact-active {
  border-bottom: 4px solid green;
  color: darkgreen;
}
</style>
