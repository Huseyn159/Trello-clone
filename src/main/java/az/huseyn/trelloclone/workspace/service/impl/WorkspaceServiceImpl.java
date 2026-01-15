package az.huseyn.trelloclone.workspace.service.impl;

import az.huseyn.trelloclone.user.entity.UserEntity;
import az.huseyn.trelloclone.user.exception.UserNotFoundException;
import az.huseyn.trelloclone.user.repository.UserRepository;
import az.huseyn.trelloclone.workspace.dto.WorkspaceCreateRequestDto;
import az.huseyn.trelloclone.workspace.dto.WorkspaceResponseDto;
import az.huseyn.trelloclone.workspace.entity.WorkspaceEntity;
import az.huseyn.trelloclone.workspace.entity.WorkspaceMembershipEntity;
import az.huseyn.trelloclone.workspace.enums.WorkspaceRole;
import az.huseyn.trelloclone.workspace.exception.*;
import az.huseyn.trelloclone.workspace.repository.WorkspaceMembershipRepository;
import az.huseyn.trelloclone.workspace.repository.WorkspaceRepository;
import az.huseyn.trelloclone.workspace.service.WorkspacePermissionService;
import az.huseyn.trelloclone.workspace.service.WorkspaceService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class WorkspaceServiceImpl implements WorkspaceService {

    private final UserRepository userRepository;
    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceMembershipRepository membershipRepository;
    private final WorkspacePermissionService workspacePermissionService;

    public WorkspaceServiceImpl(WorkspaceRepository workspaceRepository, WorkspaceMembershipRepository membershipRepository, UserRepository userRepository, WorkspacePermissionService workspacePermissionService) {
        this.workspaceRepository = workspaceRepository;
        this.membershipRepository = membershipRepository;
        this.userRepository = userRepository;
        this.workspacePermissionService = workspacePermissionService;
    }


    @Transactional
    @Override
    public WorkspaceResponseDto createWorkspace(WorkspaceCreateRequestDto workspaceCreateRequestDto,UserEntity creator) {


        WorkspaceEntity workspaceEntity = new WorkspaceEntity();
        workspaceEntity.setName(workspaceCreateRequestDto.getName());
        workspaceEntity.setCreatedAt(LocalDateTime.now());
        workspaceEntity.setDescription(workspaceCreateRequestDto.getDescription());



        workspaceRepository.save(workspaceEntity);

        WorkspaceMembershipEntity workspaceMembershipEntity = new WorkspaceMembershipEntity();
        workspaceMembershipEntity.setUser(creator);
        workspaceMembershipEntity .setRole(WorkspaceRole.OWNER);
        workspaceMembershipEntity.setJoinedAt(LocalDateTime.now());
        workspaceMembershipEntity.setWorkspace(workspaceEntity);
        membershipRepository.save(workspaceMembershipEntity);


        WorkspaceResponseDto workspaceResponseDto = new WorkspaceResponseDto();

        workspaceResponseDto.setName(workspaceEntity.getName());
        workspaceResponseDto.setId(workspaceEntity.getId());
        workspaceResponseDto.setCreatedAt(workspaceEntity.getCreatedAt());


        return workspaceResponseDto;


    }

    @Override
    public void addMember(Long workspaceId, Long memberUserId, UserEntity requester) {
        WorkspaceEntity workspaceEntity = workspaceRepository.findById(workspaceId).orElseThrow(()->
                new WorkspaceNotFoundException("Workspace not found"));

       WorkspaceMembershipEntity requesterMembership = membershipRepository.findByUserAndWorkspace(requester,workspaceEntity).orElseThrow(()->
               new MembershipNotFoundException("Membership not found"));


        if (requesterMembership.getRole() != WorkspaceRole.OWNER) {
            throw new AccessDeniedException("Only OWNER can add members");
        }

        UserEntity newMember = userRepository.findById(memberUserId).orElseThrow(()->
                new UserNotFoundException("User not found"));

        if(membershipRepository.existsByUserAndWorkspace(newMember,workspaceEntity)){
            throw new UserAlreadyInWorkspaceException("User is already in workspace");
        }

        if (newMember.getId().equals(requester.getId())) {
            throw new InvalidAddRequestException("Cannot add member to a user");
        }

        WorkspaceMembershipEntity newMembership = new WorkspaceMembershipEntity();
        newMembership.setUser(newMember);
        newMembership.setWorkspace(workspaceEntity);
        newMembership.setRole(WorkspaceRole.MEMBER);
        newMembership.setJoinedAt(LocalDateTime.now());
        membershipRepository.save(newMembership);
    }

    @Override
    public void removeMember(Long workspaceId, Long memberUserId, UserEntity requester) {
        WorkspaceEntity workspace = workspaceRepository.findById(workspaceId).orElseThrow(() ->
                new WorkspaceNotFoundException("Workspace not found"));
        workspacePermissionService.checkCanRemoveMember(requester,workspace);
        UserEntity memberUser =  userRepository.findById(memberUserId).orElseThrow(()->
                new UserNotFoundException("User not found"));
        WorkspaceMembershipEntity userMember = membershipRepository.findByUserAndWorkspace(memberUser,workspace).orElseThrow(() ->
                new MembershipNotFoundException("Membership not found")  );
        if (userMember.getRole() != WorkspaceRole.MEMBER) {
            throw new AccessDeniedException("OWNER cant be removed");
        }
        membershipRepository.delete(userMember);
    }

    @Override
    public void removeWorkspace(Long workspaceId, UserEntity requester) {
           WorkspaceEntity workspace = workspaceRepository.findById(workspaceId).orElseThrow(()->
                   new WorkspaceNotFoundException("Workspace not found"));
        workspacePermissionService.checkCanDeleteWorkspace(requester, workspace);
        workspaceRepository.delete(workspace);
    }

    @Override
    public List<WorkspaceResponseDto> getWorkspacesByUser(UserEntity user) {
        List<WorkspaceMembershipEntity> memberships =
                membershipRepository.findByUser(user);

        List<WorkspaceResponseDto> result = new ArrayList<>();

        for (WorkspaceMembershipEntity membership : memberships) {
            WorkspaceEntity w = membership.getWorkspace();

            WorkspaceResponseDto dto = new WorkspaceResponseDto();
            dto.setId(w.getId());
            dto.setName(w.getName());
            dto.setCreatedAt(w.getCreatedAt());

            result.add(dto);
        }
        return result;
    }


}
