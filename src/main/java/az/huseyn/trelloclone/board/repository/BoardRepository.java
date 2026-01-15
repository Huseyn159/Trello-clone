package az.huseyn.trelloclone.board.repository;

import az.huseyn.trelloclone.board.entity.BoardEntity;
import az.huseyn.trelloclone.workspace.entity.WorkspaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    List<BoardEntity> findByWorkspace(WorkspaceEntity workspace);


}
