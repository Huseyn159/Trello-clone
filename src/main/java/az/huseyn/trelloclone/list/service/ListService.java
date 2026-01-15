package az.huseyn.trelloclone.list.service;

import az.huseyn.trelloclone.board.entity.BoardEntity;
import az.huseyn.trelloclone.list.dto.ListCreateRequestDto;
import az.huseyn.trelloclone.list.dto.ListReorderRequestDto;
import az.huseyn.trelloclone.list.dto.ListResponseDto;
import az.huseyn.trelloclone.user.entity.UserEntity;

import java.util.List;

public interface ListService {
    ListResponseDto createList(ListCreateRequestDto listCreateRequestDto, UserEntity user);
    void reorderList(ListReorderRequestDto listReorderRequestDto);
    void deleteList(Long listId, UserEntity user);
    List<ListResponseDto> getListsByBoard(Long boardId, UserEntity user);
}
