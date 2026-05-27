export type NotificationType =
  | 'PROJECT_INVITATION'
  | 'PROJECT_INVITATION_ACCEPTED'
  | 'PROJECT_INVITATION_REJECTED'
  | 'TEAM_INVITATION'
  | 'TEAM_INVITATION_ACCEPTED'
  | 'TEAM_INVITATION_REJECTED'
  | 'TASK_ASSIGNED'
  | 'TASK_STATUS_CHANGED'

export type NotificationResourceType = 'PROJECT' | 'TEAM' | 'TASK'

export type NotificationStatus = 'PENDING' | 'READ' | 'ACCEPTED' | 'REJECTED'

export interface NotificationCreateRequest {
  username: string
  type: NotificationType
  resourceType: NotificationResourceType
  resourceId: number
}

export interface NotificationResponse {
  id: number
  senderId: number
  senderUsername: string
  recipientId: number
  recipientUsername: string
  type: NotificationType
  resourceType: NotificationResourceType
  resourceId: number
  status: NotificationStatus
  createdAt: string
}