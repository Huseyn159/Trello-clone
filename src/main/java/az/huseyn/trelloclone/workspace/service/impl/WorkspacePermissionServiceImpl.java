package az.huseyn.trelloclone.workspace.service.impl;

import az.huseyn.trelloclone.user.entity.UserEntity;
import az.huseyn.trelloclone.workspace.entity.WorkspaceEntity;
import az.huseyn.trelloclone.workspace.enums.WorkspaceRole;
import az.huseyn.trelloclone.workspace.exception.AccessDeniedException;
import az.huseyn.trelloclone.workspace.repository.WorkspaceMembershipRepository;
import az.huseyn.trelloclone.workspace.service.WorkspacePermissionService;
import org.springframework.stereotype.Service;

@Service
public class WorkspacePermissionServiceImpl implements WorkspacePermissionService {

    private final WorkspaceMembershipRepository membershipRepository;

    public WorkspacePermissionServiceImpl(WorkspaceMembershipRepository membershipRepository) {
        this.membershipRepository = membershipRepository;
    }

    private WorkspaceRole getUserRole(UserEntity user, WorkspaceEntity workspace) {
        return membershipRepository.findByUserAndWorkspace(user, workspace)
                .orElseThrow(() -> new AccessDeniedException("User is not a member of this workspace"))
                .getRole();
    }

    @Override
    public void checkCanCreateBoard(UserEntity user, WorkspaceEntity workspace) {
        WorkspaceRole role = getUserRole(user, workspace);

        if (role != WorkspaceRole.OWNER) {
            throw new AccessDeniedException("Only OWNER can create or delete boards");
        }
    }

    @Override
    public void checkCanCreateList(UserEntity user, WorkspaceEntity workspace) {
        WorkspaceRole role = getUserRole(user, workspace);

        if (role != WorkspaceRole.OWNER) {
            throw new AccessDeniedException("Only OWNER can create or delete list");
        }
    }

    @Override
    public void checkCanManageCards(UserEntity user, WorkspaceEntity workspace) {
           getUserRole(user, workspace);
    }

    @Override
    public void checkCanDeleteWorkspace(UserEntity user, WorkspaceEntity workspace) {
     WorkspaceRole role = getUserRole(user, workspace);

     if (role != WorkspaceRole.OWNER) {
         throw new AccessDeniedException("Only OWNER can delete workspaces");
     }
    }

    @Override
    public void checkCanRemoveMember(UserEntity user, WorkspaceEntity workspace) {
        WorkspaceRole role = getUserRole(user, workspace);

        if (role != WorkspaceRole.OWNER) {
            throw new AccessDeniedException("Only OWNER can remove members");
        }
    }
}