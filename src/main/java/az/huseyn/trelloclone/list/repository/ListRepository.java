package az.huseyn.trelloclone.list.repository;

import az.huseyn.trelloclone.board.entity.BoardEntity;
import az.huseyn.trelloclone.list.entity.ListEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListRepository  extends JpaRepository<ListEntity,Long> {

    List<ListEntity> findByBoardOrderByPositionDesc(BoardEntity board);
    int countByBoard(BoardEntity board);


    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
UPDATE ListEntity l
SET l.position = l.position + 1
WHERE l.board = :board
  AND l.position >= :newPosition
  AND l.position < :oldPosition
""")
    void increasePosition(
            @Param("board") BoardEntity board,
            @Param("oldPosition") int oldPosition,
            @Param("newPosition") int newPosition
    );

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
UPDATE ListEntity l
SET l.position = l.position - 1
WHERE l.board = :board
  AND l.position > :oldPosition
  AND l.position <= :newPosition
""")
    void decreasePosition(
            @Param("board") BoardEntity board,
            @Param("oldPosition") int oldPosition,
            @Param("newPosition") int newPosition
    );


    List<ListEntity> findByBoardOrderByPositionAsc(BoardEntity board);
}
