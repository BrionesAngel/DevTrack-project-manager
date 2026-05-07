import type { RouteRecordRaw } from 'vue-router'

export const projectRoutes: RouteRecordRaw[] = [
  {
    path: '/projects',
    name: 'projects',
    component: () => import('./views/ProjectsView.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/projects/create',
    name: 'project-create',
    component: () => import('./views/ProjectCreateView.vue'),
    meta: { requiresAuth: true }
  },

]
