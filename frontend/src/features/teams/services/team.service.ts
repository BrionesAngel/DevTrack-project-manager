import { privateApi } from "@/api/axios";
import type { TeamCreateResponse, TeamCreateRequest, TeamDetailsResponse, TeamRole } from "../DTOs/team.dtos";


export const teamService = {
  async createTeam(payload: TeamCreateRequest, projectId: number){
    const {data} = await  privateApi.post<TeamCreateResponse>(`/projects/${projectId}/teams`, payload)
    return data
  },
  async getTeamDetails(projectId: number, teamId: number) {
    const { data } = await privateApi.get<TeamDetailsResponse>(`/projects/${projectId}/teams/${teamId}`)
    return data
  },
  async upsertTeamMemberRole(projectId: number, teamId: number, userId: number, role: TeamRole) {
    const { data } = await privateApi.patch(`/projects/${projectId}/teams/${teamId}/members/${userId}/role`, { role })
    return data
  },
  async deleteTeam(projectId:number, teamId:number){
    const {data} = await privateApi.delete<void>(`/projects/${projectId}/teams/${teamId}`)
    return data
  }
}
