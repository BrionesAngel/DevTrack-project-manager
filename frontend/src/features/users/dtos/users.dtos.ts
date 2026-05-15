export interface UserProfileResponse{
  id: number
  userName: string
  email: string
}

export interface UpdateUsernameRequest {
  username: string
}

export interface ChangePasswordRequest {
  currentPassword: string
  newPassword: string
  confirmNewPassword: string
}
