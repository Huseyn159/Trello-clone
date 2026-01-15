package az.huseyn.trelloclone.workspace.service;

import az.huseyn.trelloclone.user.entity.UserEntity;
import az.huseyn.trelloclone.workspace.entity.WorkspaceEntity;

public interface WorkspacePermissionService {

    void checkCanCreateBoard(UserEntity user, WorkspaceEntity workspace);

    void checkCanCreateList(UserEntity user, WorkspaceEntity workspace);

    void checkCanManageCards(UserEntity user, WorkspaceEntity workspace);

    void checkCanDeleteWorkspace(UserEntity user, WorkspaceEntity workspace);

    void checkCanRemoveMember(UserEntity user, WorkspaceEntity workspace);
}