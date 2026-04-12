import { createRouter, createWebHistory } from 'vue-router'
import ProjectDashboardView from '@/features/projects/views/ProjectDashboardView.vue'
import { projectRoutes } from '@/features/projects/router'
import { kanbanRoutes } from '@/features/kanban/router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home-dashboard',
      component: ProjectDashboardView,
    },
    {
      path: '/teams',
      name: 'teams',
      component: () => import('@/features/teams/views/TeamsView.vue'),
    },
    {
      path: '/docs',
      name: 'docs',
      component: () => import('@/features/docs/views/DocsView.vue'),
    },
    ...projectRoutes,
    ...kanbanRoutes,
  ],
})

export default router
