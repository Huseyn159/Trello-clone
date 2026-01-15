package az.huseyn.trelloclone.workspace.repository;

import az.huseyn.trelloclone.workspace.entity.WorkspaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkspaceRepository extends JpaRepository<WorkspaceEntity, Long> {



}
