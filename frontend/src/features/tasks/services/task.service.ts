import { privateApi } from '@/api/axios'
import type {
  TaskAssignUserRequest,
  TaskCommentCreateRequest,
  TaskCommentResponse,
  TaskCreateRequest,
  TaskResponse,
  TaskUpdateRequest,
} from '../DTOs/task.dtos'

export const taskService = {
  async createTask(projectId: number, payload: TaskCreateRequest) {
    const { data } = await privateApi.post<TaskResponse>(`/projects/${projectId}/tasks`, payload)
    return data
  },

  async getProjectTasks(projectId: number, params?: { teamId?: number; assignedUserId?: number }) {
    const { data } = await privateApi.get<TaskResponse[]>(`/projects/${projectId}/tasks`, { params })
    return data
  },

  async updateTaskAssignee(projectId: number, taskId: number, payload: TaskAssignUserRequest) {
    const { data } = await privateApi.patch<TaskResponse>(`/projects/${projectId}/tasks/${taskId}/assignee`, payload)
    return data
  },

  async updateTask(projectId: number, taskId: number, payload: TaskUpdateRequest) {
    const { data } = await privateApi.patch<TaskResponse>(`/projects/${projectId}/tasks/${taskId}`, payload)
    return data
  },

  async getTaskComments(projectId: number, taskId: number) {
    const { data } = await privateApi.get<TaskCommentResponse[]>(`/projects/${projectId}/tasks/${taskId}/comments`)
    return data
  },

  async createTaskComment(projectId: number, taskId: number, payload: TaskCommentCreateRequest) {
    const { data } = await privateApi.post<TaskCommentResponse>(`/projects/${projectId}/tasks/${taskId}/comments`, payload)
    return data
  },
}
