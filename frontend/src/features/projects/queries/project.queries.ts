import { useMutation, useQuery, useQueryClient } from "@tanstack/vue-query";
import { projectService } from "../services/project.service";
import type { ProjectCreateRequest, UpdateProjectMemberRoleRequest } from "../DTOs/project.dtos";

export function useGetAllProjectsQuery() {
  return useQuery({
    queryKey: ['projects'],
    queryFn: () => projectService.getAllProjects(),
    staleTime: 1000 * 60 * 15,
  })
}

export function useGetAllProjectsWithTeamsQuery() {
  return useQuery({
    queryKey: ['projects', 'teams'],
    queryFn: () => projectService.getAllProjectsWithTeams(),
    staleTime: 1000 * 60 * 15,
  })
}

export function useGetProjectQuery(projectId: number) {
  return useQuery({
    queryKey: ['project', projectId],
    queryFn: () => projectService.getProject(projectId)
    ,enabled: Number.isFinite(projectId) && projectId > 0,
  })
}

export function useCreateProjectMutation() {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: (payload: ProjectCreateRequest) => projectService.createProject(payload),
    onSuccess: async () => {
      await queryClient.invalidateQueries({ queryKey: ['projects'] },)
      await queryClient.invalidateQueries({ queryKey: ['projects', 'teams'] })
      await queryClient.invalidateQueries({ queryKey: ['project'] })
      await queryClient.invalidateQueries({ queryKey: ['dashboard', 'summary'] })
    }
  })
}

export function useUpdateProjectMemberRoleMutation() {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: ({ projectId, userId, payload }: { projectId: number, userId: number, payload: UpdateProjectMemberRoleRequest }) =>
      projectService.updateProjectMemberRole(projectId, userId, payload),
    onSuccess: async () => {
      await queryClient.invalidateQueries({ queryKey: ['project'] })
    },
  })
}

export function useDeleteProjectMutation() {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: (projectId: number) => projectService.deleteProject(projectId),
    onSuccess: async () => {
      await queryClient.invalidateQueries({ queryKey: ['projects'] })
      await queryClient.invalidateQueries({ queryKey: ['projects', 'teams'] })
      await queryClient.invalidateQueries({ queryKey: ['project'] })
    }
  })
}
