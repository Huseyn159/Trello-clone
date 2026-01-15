package az.huseyn.trelloclone.list.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListCreateRequestDto {

    @NotBlank(message = "List name must be filled")
    @Size(max = 100,message = "Maximum 100 characters allowed")
    private String listName;

    @NotNull
    private Long boardId;

}
