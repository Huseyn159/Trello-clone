package az.huseyn.trelloclone.card.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CardResponseDto {

    private Long cardId;
    private String cardName;
    private LocalDateTime createdAt;
    private Long listId;
    private int position;


}
