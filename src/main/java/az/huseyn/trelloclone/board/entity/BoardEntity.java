package az.huseyn.trelloclone.board.entity;

import az.huseyn.trelloclone.list.entity.ListEntity;
import az.huseyn.trelloclone.workspace.entity.WorkspaceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "app_boards")
public class BoardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private LocalDateTime createdAt;
    @ManyToOne
    @JoinColumn(name = "workspace_id",nullable = false)
    private WorkspaceEntity workspace;

    @OneToMany(
            mappedBy = "board",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ListEntity> lists;

}
