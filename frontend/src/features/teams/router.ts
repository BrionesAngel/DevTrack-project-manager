import type { RouteRecordRaw } from 'vue-router'

export const teamsRoutes: RouteRecordRaw[] = [
  {
    path: '/teams',
    name: 'teams',
    component: () => import('@/features/teams/views/TeamsView.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/projects/:projectId/teams',
    name: 'team-create',
    component: () => import('@/features/teams/views/TeamCreateView.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/projects/:projectId/teams/:teamId',
    name: 'team-details',
    component: () => import('@/features/teams/views/TeamDetailsView.vue'),
    meta: { requiresAuth: true }
  },

]

