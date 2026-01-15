package az.huseyn.trelloclone.board.service.impl;

import az.huseyn.trelloclone.board.dto.BoardCreateRequestDto;
import az.huseyn.trelloclone.board.dto.BoardResponseDto;
import az.huseyn.trelloclone.board.entity.BoardEntity;
import az.huseyn.trelloclone.board.exception.BoardNotFoundException;
import az.huseyn.trelloclone.board.repository.BoardRepository;
import az.huseyn.trelloclone.board.service.BoardService;
import az.huseyn.trelloclone.user.entity.UserEntity;
import az.huseyn.trelloclone.workspace.entity.WorkspaceEntity;
import az.huseyn.trelloclone.workspace.exception.WorkspaceNotFoundException;
import az.huseyn.trelloclone.workspace.repository.WorkspaceRepository;
import az.huseyn.trelloclone.workspace.service.WorkspaceService;
import az.huseyn.trelloclone.workspace.service.impl.WorkspacePermissionServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;
    private final WorkspaceRepository workspaceRepository;
    private final WorkspacePermissionServiceImpl workspacePermissionService;
    public BoardServiceImpl(BoardRepository boardRepository, WorkspaceRepository workspaceRepository, WorkspaceService workspaceService, WorkspacePermissionServiceImpl workspacePermissionService) {
        this.boardRepository = boardRepository;
        this.workspaceRepository = workspaceRepository;

        this.workspacePermissionService = workspacePermissionService;
    }


    @Override
    public BoardResponseDto createBoard(BoardCreateRequestDto boardCreateRequestDto, UserEntity user) {
        BoardEntity boardEntity = new BoardEntity();
        WorkspaceEntity workspaceEntity = workspaceRepository.findById(boardCreateRequestDto.getWorkspaceId())
                .orElseThrow(() -> new WorkspaceNotFoundException("Workspace not found"));
        workspacePermissionService.checkCanCreateBoard(user, workspaceEntity);
        boardEntity.setTitle(boardCreateRequestDto.getTitle());
        boardEntity.setCreatedAt(LocalDateTime.now());
        boardEntity.setWorkspace(workspaceEntity);
        boardRepository.save(boardEntity);

        BoardResponseDto boardResponseDto = new BoardResponseDto();
        boardResponseDto.setId(boardEntity.getId());
        boardResponseDto.setTitle(boardEntity.getTitle());
        boardResponseDto.setCreatedAt(boardEntity.getCreatedAt());
        boardResponseDto.setWorkspaceId(boardEntity.getWorkspace().getId());

        return boardResponseDto;

    }

    @Override
    public List<BoardResponseDto>  getBoardsByWorkspace(Long workspaceId ) {
        WorkspaceEntity workspaceEntity = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new WorkspaceNotFoundException("Workspace not found"));
        List<BoardEntity> boardEntities =  boardRepository.findByWorkspace(workspaceEntity);
        List<BoardResponseDto> boardResponseDtos = new ArrayList<>();
        for (BoardEntity boardEntity : boardEntities) {
            BoardResponseDto boardResponseDto = new BoardResponseDto();
            boardResponseDto.setId(boardEntity.getId());
            boardResponseDto.setTitle(boardEntity.getTitle());
            boardResponseDto.setCreatedAt(boardEntity.getCreatedAt());
            boardResponseDto.setWorkspaceId(boardEntity.getWorkspace().getId());
            boardResponseDtos.add(boardResponseDto);
        }
         return boardResponseDtos;

    }

    @Override
    public void deleteBoard(Long boardId, UserEntity user) {
        BoardEntity board = boardRepository.findById(boardId).orElseThrow(()-> new BoardNotFoundException("Board not found"));
        WorkspaceEntity workspace = board.getWorkspace();
        workspacePermissionService.checkCanCreateBoard(user, workspace);
        boardRepository.delete(board);
    }
}
