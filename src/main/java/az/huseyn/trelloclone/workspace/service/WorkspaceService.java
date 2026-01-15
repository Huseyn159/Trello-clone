package az.huseyn.trelloclone.workspace.service;

import az.huseyn.trelloclone.user.entity.UserEntity;
import az.huseyn.trelloclone.workspace.dto.WorkspaceCreateRequestDto;
import az.huseyn.trelloclone.workspace.dto.WorkspaceResponseDto;

import java.util.List;

public interface WorkspaceService {

    WorkspaceResponseDto createWorkspace(WorkspaceCreateRequestDto workspaceCreateRequestDto, UserEntity creator);
    void addMember(Long workspaceId, Long memberUserId, UserEntity requester);
    void removeMember(Long workspaceId, Long memberUserId, UserEntity requester);
    void removeWorkspace(Long workspaceId, UserEntity requester);
    List<WorkspaceResponseDto> getWorkspacesByUser(UserEntity user);
}
