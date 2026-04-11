import { createRouter, createWebHistory } from 'vue-router'
import ProjectDashboardView from '@/features/projects/views/ProjectDashboardView.vue'
import { projectRoutes } from '@/features/projects/router'

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
    {
      path: '/kanban',
      name: 'kanban',
      component: () => import('@/features/kanban/views/KanbanView.vue'),
    },
    ...projectRoutes,
  ],
})

export default router
