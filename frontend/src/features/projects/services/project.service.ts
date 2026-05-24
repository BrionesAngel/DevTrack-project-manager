import { privateApi } from "@/api/axios";
import type { ProjectCreateRequest, ProjectCreateResponse, ProjectResponse } from "../DTOs/project.dtos";


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
    const { data } = await privateApi.get<ProjectResponse[]>('/projects')
    return data
  }
}
