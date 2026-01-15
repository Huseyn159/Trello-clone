package az.huseyn.trelloclone.workspace.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddWorkspaceMemberRequestDto {

    @NotNull
    Long userId;
}
