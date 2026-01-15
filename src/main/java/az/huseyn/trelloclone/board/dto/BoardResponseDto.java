package az.huseyn.trelloclone.board.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class BoardResponseDto {

    private Long id;
    private String title;
    private LocalDateTime createdAt;
    private Long workspaceId;



}
