package az.huseyn.trelloclone.workspace.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkspaceCreateRequestDto {


    @NotBlank(message = "Name must be filled")
    @Size(max = 50,message = "Maximum 50 characters allowed")
    private String name;

    @Size(max = 500,message = "Maximum 500 characters allowed")
    private String description;
}
