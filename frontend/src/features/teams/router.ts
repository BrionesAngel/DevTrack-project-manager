import type { RouteRecordRaw } from 'vue-router'

export const teamsRoutes: RouteRecordRaw[] = [
  {
    path: '/teams',
    name: 'teams',
    component: () => import('@/features/teams/views/TeamsView.vue'),
    meta: { requiresAuth: true }
  },

]

