import type { ProjectMemberOverview } from "@/features/projects/DTOs/project.dtos"

export type TeamRole = 'LEAD' | 'MEMBER'

export interface TeamCreateRequest{
  name: string
}

export interface TeamCreateResponse{
  id: number
  name: string
}

export interface TeamMemberOverview {
  userId: number
  username: string
  role: TeamRole
}

export interface TeamOverview {
  id: number
  name: string
  members: number
}

export interface TeamDetailsResponse {
  id: number
  name: string
  projectId: number
  members: TeamMemberOverview[]
  projectMembers: ProjectMemberOverview[]
}

