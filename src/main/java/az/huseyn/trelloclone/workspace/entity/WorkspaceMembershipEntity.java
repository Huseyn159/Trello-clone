package az.huseyn.trelloclone.workspace.entity;

import az.huseyn.trelloclone.user.entity.UserEntity;
import az.huseyn.trelloclone.workspace.enums.WorkspaceRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "app_workspace_memberships")
public class WorkspaceMembershipEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id")
    private WorkspaceEntity workspace;

    @Enumerated(EnumType.STRING)
    private WorkspaceRole role;

    private LocalDateTime joinedAt;


}
