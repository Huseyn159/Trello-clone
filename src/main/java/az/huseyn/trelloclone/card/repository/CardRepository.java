package az.huseyn.trelloclone.card.repository;

import az.huseyn.trelloclone.card.entity.CardEntity;
import az.huseyn.trelloclone.list.entity.ListEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CardRepository extends JpaRepository<CardEntity, Long> {
   List<CardEntity> findByListEntityOrderByPositionAsc(ListEntity listEntity);
   int countByListEntity(ListEntity listEntity);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        UPDATE CardEntity c
        SET c.position = c.position + 1
        WHERE c.listEntity = :list
          AND c.position >= :newPosition
          AND c.position <  :oldPosition
          AND c.id <> :cardId
    """)
    void increasePosition(
            @Param("list") ListEntity list,
            @Param("newPosition") int newPosition,
            @Param("oldPosition") int oldPosition,
            @Param("cardId") Long cardId
    );

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        UPDATE CardEntity c
        SET c.position = c.position - 1
        WHERE c.listEntity = :list
          AND c.position >  :oldPosition
          AND c.position <= :newPosition
          AND c.id <> :cardId
    """)
    void decreasePosition(
            @Param("list") ListEntity list,
            @Param("oldPosition") int oldPosition,
            @Param("newPosition") int newPosition,
            @Param("cardId") Long cardId
    );


}
