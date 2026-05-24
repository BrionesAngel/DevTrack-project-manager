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
export interface ProjectResponse {
  id: number
  title: string
  description: string
  createdBy: string
  teams: number
  members: number
}
