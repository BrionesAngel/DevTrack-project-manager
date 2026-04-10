<template>
  <div>
    <ul class="w-full">
      <li v-for="i in 5" :key="i">
        <div class="m-12" @click="">
          <TeamsProjectsCard></TeamsProjectsCard>
        </div>
      </li>
    </ul>
  </div>
</template>

<script setup lang="ts">
import TeamsProjectsCard from '../components/TeamsProjectsCard.vue';


</script>
