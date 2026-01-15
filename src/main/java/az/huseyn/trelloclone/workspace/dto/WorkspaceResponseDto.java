package az.huseyn.trelloclone.workspace.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class WorkspaceResponseDto {

    private String name;
    private Long id;
    private LocalDateTime createdAt;

}
