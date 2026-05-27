export type TaskPriority = 'LOW' | 'MEDIUM' | 'HIGH'

export type TaskStatus = 'UNASSIGNED' | 'ASSIGNED' | 'IN_PROGRESS' | 'REVIEW' | 'REJECTED' | 'COMPLETED'

export interface TaskCreateRequest {
  title: string
  description: string
  priority?: TaskPriority
  dueDate?: string | null
  githubIssueUrl?: string | null
  comment?: string | null
  assignedUser?: number | null
  teamId: number
}

export interface TaskResponse {
  id: number
  title: string
  description: string
  priority: TaskPriority
  status: TaskStatus
  dueDate: string | null
  assignedUserId: number | null
  assignedUsername: string | null
  assignedTeamId: number
  assignedTeamName: string
  projectId: number
  githubIssueUrl?: string | null
}

export interface TaskAssignUserRequest {
  assignedUserId: number
}

export interface TaskUpdateRequest {
  priority?: TaskPriority
  status?: TaskStatus
  dueDate?: string | null
  assignedUserId?: number
  githubIssueUrl?: string | null
  comment?: string | null
}

export interface TaskCommentResponse {
  id: number
  authorId: number
  authorUsername: string
  action: string
  message: string
  createdAt: string
}

export interface TaskCommentCreateRequest {
  message: string
}
