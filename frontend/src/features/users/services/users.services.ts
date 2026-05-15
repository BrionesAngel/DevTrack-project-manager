import { privateApi } from "@/api/axios"
import type { ChangePasswordRequest, UpdateUsernameRequest, UserProfileResponse } from "../dtos/users.dtos"

export const usersService = {
  async getMyProfile(){
    const {data} = await privateApi.get<UserProfileResponse>(`/users/profile`)
    return data;
  },

  async updateMyUsername(payload: UpdateUsernameRequest) {
    const { data } = await privateApi.patch<UserProfileResponse>('/users/profile/username', payload)
    return data
  },

  async changeMyPassword(payload: ChangePasswordRequest) {
    const { data } = await privateApi.patch<void>('/users/profile/password', payload)
    return data
  }
}
