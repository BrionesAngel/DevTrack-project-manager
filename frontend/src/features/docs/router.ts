import type { RouteRecordRaw } from 'vue-router'

export const docsRoutes: RouteRecordRaw[] = [
  {
    path: '/docs',
    name: 'docs',
    component: () => import('@/features/docs/views/DocsView.vue'),
    meta: { requiresAuth: true }
  },
]
