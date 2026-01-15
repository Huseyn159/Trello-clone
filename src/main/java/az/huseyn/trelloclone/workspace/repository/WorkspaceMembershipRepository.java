package az.huseyn.trelloclone.workspace.repository;

import az.huseyn.trelloclone.user.entity.UserEntity;
import az.huseyn.trelloclone.workspace.entity.WorkspaceEntity;
import az.huseyn.trelloclone.workspace.entity.WorkspaceMembershipEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkspaceMembershipRepository extends JpaRepository<WorkspaceMembershipEntity, Long> {

    //user workspace-de varmi
    boolean existsByUserAndWorkspace(UserEntity user, WorkspaceEntity workspace);

    //for permission
    Optional<WorkspaceMembershipEntity>
    findByUserAndWorkspace(UserEntity user, WorkspaceEntity workspace);


    List<WorkspaceMembershipEntity> findByUser(UserEntity user);
}
