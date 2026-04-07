import { createRouter, createWebHistory } from 'vue-router'
import ProjectDashboardView from '@/features/projects/views/ProjectDashboardView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home-dashboard',
      component: ProjectDashboardView,
    },
  ],
})

export default router
