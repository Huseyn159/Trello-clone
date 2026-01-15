package az.huseyn.trelloclone.card.entity;

import az.huseyn.trelloclone.list.entity.ListEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "app_cards")
public class CardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cardName;
    private int position;
    private LocalDateTime createdAt;


    @ManyToOne
    @JoinColumn(name = "list_id",nullable = false)
    private ListEntity listEntity;

}
