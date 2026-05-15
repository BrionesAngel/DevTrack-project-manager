import { createRouter, createWebHistory } from 'vue-router'
import ProjectDashboardView from '@/features/projects/views/ProjectDashboardView.vue'
import { useAuthStore } from '@/features/auth/stores/auth.store.ts'
import { authRoutes } from '@/features/auth/routes'
import { projectRoutes } from '@/features/projects/router'
import { kanbanRoutes } from '@/features/kanban/router'
import { taskRoutes } from '@/features/tasks/router'
import { docsRoutes } from '@/features/docs/router'
import { teamsRoutes } from '@/features/teams/router'
import { userRoutes } from '@/features/users/router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home-dashboard',
      component: ProjectDashboardView,
      meta: { requiresAuth: true }
    },
    ...authRoutes,
    ...projectRoutes,
    ...taskRoutes,
    ...kanbanRoutes,
    ...teamsRoutes,
    ...docsRoutes,
    ...userRoutes,
  ],
})

router.beforeEach((to) => {
  const authStore = useAuthStore()
  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    return { name: 'login' }
  }

  if ((to.name === 'login' || to.name === 'register') && authStore.isAuthenticated) {
    return { name: 'home-dashboard' }
  }

  return true
})

export default router
