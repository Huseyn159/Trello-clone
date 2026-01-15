package az.huseyn.trelloclone.list.service.impl;

import az.huseyn.trelloclone.board.entity.BoardEntity;
import az.huseyn.trelloclone.board.exception.BoardNotFoundException;
import az.huseyn.trelloclone.board.repository.BoardRepository;
import az.huseyn.trelloclone.list.dto.ListCreateRequestDto;
import az.huseyn.trelloclone.list.dto.ListReorderRequestDto;
import az.huseyn.trelloclone.list.dto.ListResponseDto;
import az.huseyn.trelloclone.list.entity.ListEntity;
import az.huseyn.trelloclone.list.exception.InvalidPositionException;
import az.huseyn.trelloclone.list.exception.ListNotFoundException;
import az.huseyn.trelloclone.list.repository.ListRepository;
import az.huseyn.trelloclone.list.service.ListService;
import az.huseyn.trelloclone.user.entity.UserEntity;
import az.huseyn.trelloclone.workspace.entity.WorkspaceEntity;
import az.huseyn.trelloclone.workspace.service.WorkspacePermissionService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ListServiceImpl implements ListService {
   private final ListRepository listRepository;
   private final BoardRepository  boardRepository;
   private final WorkspacePermissionService workspacePermissionService;
    public ListServiceImpl(ListRepository listRepository, BoardRepository boardRepository, WorkspacePermissionService workspacePermissionService) {
        this.listRepository = listRepository;
        this.boardRepository = boardRepository;
        this.workspacePermissionService = workspacePermissionService;
    }


    @Override
    public ListResponseDto createList(ListCreateRequestDto listCreateRequestDto, UserEntity user) {
        BoardEntity boardEntity = boardRepository.findById(listCreateRequestDto.getBoardId())
                .orElseThrow(() -> new BoardNotFoundException("Board not found") );
        workspacePermissionService.checkCanCreateList(user,boardEntity.getWorkspace());
        List<ListEntity> lists = listRepository.findByBoardOrderByPositionDesc(boardEntity);
        ListEntity listEntity = new ListEntity();
        listEntity.setListName(listCreateRequestDto.getListName());
        listEntity.setBoard(boardEntity);
        listEntity.setCreatedAt(LocalDateTime.now());
        if (lists.isEmpty()){
            listEntity.setPosition(1);
        }else listEntity.setPosition(lists.get(0).getPosition()+1);
        listRepository.save(listEntity);



        ListResponseDto responseDto = new ListResponseDto();

            responseDto.setId(listEntity.getId());
            responseDto.setListName(listEntity.getListName());
            responseDto.setBoardId(listEntity.getBoard().getId());
            responseDto.setPosition(listEntity.getPosition());
            responseDto.setCreatedAt(listEntity.getCreatedAt());


        return responseDto;
    }

    @Transactional
    @Override
    public void reorderList(ListReorderRequestDto listReorderRequestDto) {
        int newPosition = listReorderRequestDto.getNewPosition();
        Long listId = listReorderRequestDto.getListId();
        ListEntity listEntity = listRepository.findById(listId)
                .orElseThrow(() -> new ListNotFoundException("List not found") );

        int oldPosition = listEntity.getPosition();
        BoardEntity board = listEntity.getBoard();
        int maxPosition = listRepository.countByBoard(board);

        if (newPosition == oldPosition) return;
        if (newPosition < 1 || newPosition > maxPosition) {
            throw new InvalidPositionException("Position must be between 1 and " + maxPosition);
        }
        if (newPosition < oldPosition) {
            listRepository.increasePosition(board,oldPosition,newPosition);
        }
        else {
            listRepository.decreasePosition(board,oldPosition,newPosition);
        }

        listEntity.setPosition(newPosition);
        listRepository.save(listEntity);


    }

    @Override
    public void deleteList(Long listId, UserEntity user) {
        ListEntity list =  listRepository.findById(listId)
                .orElseThrow(() -> new ListNotFoundException("List not found") );
        BoardEntity board = list.getBoard();
        WorkspaceEntity workspace = board.getWorkspace();

        workspacePermissionService.checkCanCreateList(user,board.getWorkspace());
        int oldPosition = list.getPosition();
        int maxPosition = listRepository.countByBoard(board);
        listRepository.decreasePosition(board,oldPosition,maxPosition);
        listRepository.delete(list);
    }

    @Override
    public List<ListResponseDto> getListsByBoard(Long boardId, UserEntity user) {
        BoardEntity board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException("Board not found"));

        workspacePermissionService.checkCanManageCards(
                user,
                board.getWorkspace()
        );

        List<ListEntity> lists =
                listRepository.findByBoardOrderByPositionAsc(board);

        List<ListResponseDto> result = new ArrayList<>();

        for (ListEntity list : lists) {
            ListResponseDto dto = new ListResponseDto();
            dto.setId(list.getId());
            dto.setListName(list.getListName());
            dto.setPosition(list.getPosition());
            dto.setBoardId(boardId);

            result.add(dto);
        }
        return result;
    }
}
