import { useQuery } from "@tanstack/vue-query";
import { useMutation, useQueryClient } from '@tanstack/vue-query'
import { usersService } from "./users.services";
import type { ChangePasswordRequest, UpdateUsernameRequest } from "../dtos/users.dtos";

export function useUserProfileQuery(){
  return useQuery({
    queryKey: ["users", "profile"],
    queryFn: () => usersService.getMyProfile(),
    staleTime: 1000 * 60 * 5,
    retry: 1,
  })
}

export function useUpdateUsernameMutation() {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: (payload: UpdateUsernameRequest) => usersService.updateMyUsername(payload),
    onSuccess: async () => {
      await queryClient.invalidateQueries({ queryKey: ['users', 'profile'] })
    },
  })
}

export function useChangePasswordMutation() {
  return useMutation({
    mutationFn: (payload: ChangePasswordRequest) => usersService.changeMyPassword(payload),
  })
}
