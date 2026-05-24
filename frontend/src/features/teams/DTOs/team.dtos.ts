export type TeamRole = 'LEAD' | 'MEMBER'

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

