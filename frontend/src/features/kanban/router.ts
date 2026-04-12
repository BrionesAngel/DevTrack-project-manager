import type { RouteRecordRaw } from 'vue-router'

export const kanbanRoutes: RouteRecordRaw[] = [
  {
    path: '/kanban',
    name: 'kanban',
    component: () => import('./views/KanbanView.vue'),
  },
  {
    path: '/kanban/view',
    name: 'project-kanban-view',
    component: () => import('./views/KanbanTasksView.vue'),
  },

]
