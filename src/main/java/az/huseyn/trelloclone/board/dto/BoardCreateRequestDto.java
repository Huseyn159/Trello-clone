package az.huseyn.trelloclone.board.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardCreateRequestDto {

    @NotBlank(message = "Title must be filled")
    @Size(max = 30,message = "Title  cannot contain more than 30 characters")
    private String title;

    @NotNull(message = "Workspace id must be provided")
    private Long workspaceId;

}
