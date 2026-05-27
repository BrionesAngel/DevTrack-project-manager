import type { TeamOverview } from "@/features/teams/DTOs/team.dtos"

export type ProjectRole = 'OWNER' | 'ADMIN' | 'MEMBER'

export interface ProjectMemberOverview {
  userId: number
  username: string
  role: ProjectRole
}
export interface ProjectCreateRequest {
  title: string
  description: string
}
export interface ProjectCreateResponse {
  id: number
  title: string
  description: string
  createdBy: string
}

export interface ProjectOverviewResponse {
  id: number
  title: string
  createdBy: string
  teams: number
  members: number
}

export interface ProjectResponse {
  id: number
  title: string
  description: string
  createdBy: string
  teams: TeamOverview[]
  members: ProjectMemberOverview[]
}

export interface ProjectTeamsResponse {
  id: number
  title: string
  createdBy: string
  teams: TeamOverview[]
}

export interface UpdateProjectMemberRoleRequest {
  role: ProjectRole
}
