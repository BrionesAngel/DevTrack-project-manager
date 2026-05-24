import { useMutation, useQuery, useQueryClient } from "@tanstack/vue-query";
import { projectService } from "../services/project.service";
import type { ProjectCreateRequest } from "../DTOs/project.dtos";

export function useGetAllProjectsQuery(){
  return useQuery({
    queryKey: ['projects'],
    queryFn: () => projectService.getAllProjects()
  })
}

export function useGetProjectQuery(projectId: number){
  return useQuery({
    queryKey: ['project', projectId],
    queryFn: () => projectService.getProject(projectId)
  })
}

export function useCreateProjectMutation(){
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: (payload: ProjectCreateRequest) => projectService.createProject(payload),
    onSuccess: async () => {
      await queryClient.invalidateQueries({queryKey: ['projects']})
    }
  })
}
