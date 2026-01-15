package az.huseyn.trelloclone.list.entity;

import az.huseyn.trelloclone.board.entity.BoardEntity;
import az.huseyn.trelloclone.card.entity.CardEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "app_lists")
public class ListEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String listName;
    private int position;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "board_id",nullable = false)
    private BoardEntity board;

    @OneToMany(
            mappedBy = "listEntity",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<CardEntity> cards;




}
