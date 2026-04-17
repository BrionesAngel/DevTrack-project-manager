import type { RouteRecordRaw } from 'vue-router'

export const taskRoutes: RouteRecordRaw[] = [
  {
    path: '/tasks',
    name: 'tasks',
    component: () => import('./views/TasksProjectsView.vue'),
  },
  {
    path: '/tasks/create',
    name: 'task-create',
    component: () => import('./views/TaskCreateView.vue'),
  },
  {
    path: '/tasks/project/:projectId',
    name: 'tasks-by-project',
    component: () => import('./views/TasksByProjectView.vue'),
  },
  {
    path: '/tasks/project/:projectId/create',
    name: 'task-create-by-project',
    component: () => import('./views/TaskCreateView.vue'),
  },
]
