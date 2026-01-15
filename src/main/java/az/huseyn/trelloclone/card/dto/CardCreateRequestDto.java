package az.huseyn.trelloclone.card.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardCreateRequestDto {

    @NotBlank(message = "Card name must be filled")
    @Size(max = 50,message = "Maximum 50 characters allowed")
    private String cardName;

    @NotNull
    private Long listId;


}
