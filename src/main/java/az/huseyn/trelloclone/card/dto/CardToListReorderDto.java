package az.huseyn.trelloclone.card.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardToListReorderDto {

    @NotNull
    private Long cardId;

    @NotNull
    private Long targetListId;

    @Min(1)
    private int newPosition;






}
