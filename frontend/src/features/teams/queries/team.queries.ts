import { useMutation, useQuery, useQueryClient } from "@tanstack/vue-query"
import type { TeamCreateRequest, TeamRole } from "../DTOs/team.dtos"
import { teamService } from "../services/team.service"

export function useGetTeamDetailsQuery(projectId: number, teamId: number) {
  return useQuery({
    queryKey: ['team', projectId, teamId],
    queryFn: () => teamService.getTeamDetails(projectId, teamId),
    enabled: Number.isFinite(projectId) && projectId > 0 && Number.isFinite(teamId) && teamId > 0,
  })
}

export function useCreateTeamMutation() {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: ({ payload, projectId }: { payload: TeamCreateRequest, projectId: number }) => teamService.createTeam(payload, projectId),
    onSuccess: async () => {
      await queryClient.invalidateQueries({ queryKey: ['projects'] },)
      await queryClient.invalidateQueries({ queryKey: ['projects', 'teams'] })
      await queryClient.invalidateQueries({ queryKey: ['project'] })
    }
  })
}

export function useDeleteTeamMutation() {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: ({ projectId, teamId }: { projectId: number, teamId: number }) => teamService.deleteTeam(projectId, teamId),
    onSuccess: async () => {
      await queryClient.invalidateQueries({ queryKey: ['projects'] },)
      await queryClient.invalidateQueries({ queryKey: ['projects', 'teams'] })
      await queryClient.invalidateQueries({ queryKey: ['project'] })
    }
  })
}

export function useUpdateTeamMemberRoleMutation() {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: ({ projectId, teamId, userId, role }: { projectId: number, teamId: number, userId: number, role: TeamRole }) =>
      teamService.upsertTeamMemberRole(projectId, teamId, userId, role),
    onSuccess: async (_data, variables) => {
      await queryClient.invalidateQueries({ queryKey: ['team', variables.projectId, variables.teamId] })
      await queryClient.invalidateQueries({ queryKey: ['project', variables.projectId] })
      await queryClient.invalidateQueries({ queryKey: ['projects'] })
      await queryClient.invalidateQueries({ queryKey: ['projects', 'teams'] })
    },
  })
}

