import type { RouteRecordRaw } from 'vue-router'

export const userRoutes: RouteRecordRaw[] = [
  {
    path: '/profile',
    name: 'user-profile',
    component: () => import('./views/UserProfileView.vue'),
    meta: { requiresAuth: true },
  },
  {
    path: '/settings',
    name: 'user-settings',
    component: () => import('./views/UserSettingsView.vue'),
    meta: { requiresAuth: true },
  },
]