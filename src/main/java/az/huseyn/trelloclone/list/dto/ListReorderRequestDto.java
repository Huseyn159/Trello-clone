package az.huseyn.trelloclone.list.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListReorderRequestDto {

    @NotNull()
    private Long listId;

    @Min(1)
    private int newPosition;

}
