package az.huseyn.trelloclone.list.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ListResponseDto {

    private Long id;
    private String listName;
    private int position;
    private LocalDateTime createdAt;
    private Long boardId;



}
