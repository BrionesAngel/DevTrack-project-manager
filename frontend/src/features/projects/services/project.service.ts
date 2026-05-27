import { privateApi } from "@/api/axios";
import type { ProjectCreateRequest, ProjectCreateResponse, ProjectMemberOverview, ProjectOverviewResponse, ProjectResponse, ProjectTeamsResponse, UpdateProjectMemberRoleRequest } from "../DTOs/project.dtos";


export const projectService = {
  async getProject(projectId: number) {
    const { data } = await privateApi.get<ProjectResponse>(`/projects/${projectId}`)
    return data
  },

  async createProject(payload: ProjectCreateRequest) {
    const { data } = await privateApi.post<ProjectCreateResponse>('/projects', payload)
    return data
  },

  async getAllProjects() {
    const { data } = await privateApi.get<ProjectOverviewResponse[]>('/projects')
    return data
  },
  async getAllProjectsWithTeams() {
    const { data } = await privateApi.get<ProjectTeamsResponse[]>('/projects/with-teams')
    return data
  },

  async updateProjectMemberRole(projectId: number, userId: number, payload: UpdateProjectMemberRoleRequest) {
    const { data } = await privateApi.patch<ProjectMemberOverview>(`/projects/${projectId}/members/${userId}/role`, payload)
    return data
  }
  ,
  async deleteProject(projectId: number) {
    const { data } = await privateApi.delete(`/projects/${projectId}`)
    return data
  }
}
