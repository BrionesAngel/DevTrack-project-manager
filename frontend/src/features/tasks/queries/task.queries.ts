import { useMutation, useQuery, useQueryClient } from '@tanstack/vue-query'
import { taskService } from '../services/task.service'
import type {
  TaskAssignUserRequest,
  TaskCommentCreateRequest,
  TaskCreateRequest,
  TaskUpdateRequest,
} from '../DTOs/task.dtos'

export function useCreateTaskMutation() {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: ({ projectId, payload }: { projectId: number; payload: TaskCreateRequest }) =>
      taskService.createTask(projectId, payload),
    onSuccess: async (_data, variables) => {
      await queryClient.invalidateQueries({ queryKey: ['tasks', variables.projectId] })
      await queryClient.invalidateQueries({ queryKey: ['project', variables.projectId] })
      await queryClient.invalidateQueries({ queryKey: ['projects'] })
    },
  })
}

export function useGetProjectTasksQuery(projectId: number, teamId?: number, assignedUserId?: number) {
  return useQuery({
    queryKey: ['tasks', projectId, teamId ?? null, assignedUserId ?? null],
    queryFn: () => taskService.getProjectTasks(projectId, { teamId, assignedUserId }),
    enabled: Number.isFinite(projectId) && projectId > 0,
  })
}

export function useUpdateTaskAssigneeMutation() {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: ({ projectId, taskId, payload }: { projectId: number; taskId: number; payload: TaskAssignUserRequest }) =>
      taskService.updateTaskAssignee(projectId, taskId, payload),
    onSuccess: async (_data, variables) => {
      await queryClient.invalidateQueries({ queryKey: ['tasks', variables.projectId] })
      await queryClient.invalidateQueries({ queryKey: ['project', variables.projectId] })
    },
  })
}

export function useUpdateTaskMutation() {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: ({ projectId, taskId, payload }: { projectId: number; taskId: number; payload: TaskUpdateRequest }) =>
      taskService.updateTask(projectId, taskId, payload),
    onSuccess: async (_data, variables) => {
      await queryClient.invalidateQueries({ queryKey: ['tasks', variables.projectId] })
      await queryClient.invalidateQueries({ queryKey: ['project', variables.projectId] })
      await queryClient.invalidateQueries({ queryKey: ['task-comments', variables.projectId, variables.taskId] })
    },
  })
}

export function useGetTaskCommentsQuery(projectId: number, taskId: number) {
  return useQuery({
    queryKey: ['task-comments', projectId, taskId],
    queryFn: () => taskService.getTaskComments(projectId, taskId),
    enabled: Number.isFinite(projectId) && projectId > 0 && Number.isFinite(taskId) && taskId > 0,
  })
}

export function useCreateTaskCommentMutation() {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: ({ projectId, taskId, payload }: { projectId: number; taskId: number; payload: TaskCommentCreateRequest }) =>
      taskService.createTaskComment(projectId, taskId, payload),
    onSuccess: async (_data, variables) => {
      await queryClient.invalidateQueries({ queryKey: ['task-comments', variables.projectId, variables.taskId] })
      await queryClient.invalidateQueries({ queryKey: ['tasks', variables.projectId] })
    },
  })
}
