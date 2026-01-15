package az.huseyn.trelloclone.board.service;

import az.huseyn.trelloclone.board.dto.BoardCreateRequestDto;
import az.huseyn.trelloclone.board.dto.BoardResponseDto;
import az.huseyn.trelloclone.board.entity.BoardEntity;
import az.huseyn.trelloclone.user.entity.UserEntity;
import az.huseyn.trelloclone.workspace.entity.WorkspaceEntity;

import java.util.List;

public interface BoardService {

    BoardResponseDto createBoard(BoardCreateRequestDto boardCreateRequestDto, UserEntity user);
    List<BoardResponseDto>  getBoardsByWorkspace(Long workspaceId);
    void deleteBoard(Long boardId, UserEntity user);
}
