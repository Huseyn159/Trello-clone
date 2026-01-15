package az.huseyn.trelloclone.board.controller;


import az.huseyn.trelloclone.board.dto.BoardCreateRequestDto;
import az.huseyn.trelloclone.board.dto.BoardResponseDto;
import az.huseyn.trelloclone.board.service.BoardService;
import az.huseyn.trelloclone.list.dto.ListResponseDto;
import az.huseyn.trelloclone.list.service.ListService;
import az.huseyn.trelloclone.user.entity.UserEntity;
import az.huseyn.trelloclone.user.exception.UserNotFoundException;
import az.huseyn.trelloclone.user.repository.UserRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Board", description = "Board operations")

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    private final UserRepository userRepository;
    private final BoardService boardService;
    private final ListService listService;


    public BoardController(UserRepository userRepository, BoardService boardService, ListService listService) {
        this.userRepository = userRepository;
        this.boardService = boardService;
        this.listService = listService;
    }


    @PostMapping
    public ResponseEntity<BoardResponseDto> createBoard(
            @Valid @RequestBody BoardCreateRequestDto boardCreateRequestDto,
            @RequestParam Long userId) {

        UserEntity requester = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Requester not found"));
        BoardResponseDto boardResponseDto = boardService.createBoard(boardCreateRequestDto,requester);
        return ResponseEntity.status(HttpStatus.CREATED).body(boardResponseDto);
    }

    @GetMapping("/{boardId}/lists")
    public ResponseEntity<List<ListResponseDto>> getListsByBoard(
            @PathVariable Long boardId,
            @RequestParam Long userId
    ) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return ResponseEntity.ok(
                listService.getListsByBoard(boardId, user)
        );
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long boardId,
                                            @RequestParam Long userId) {
        UserEntity requester = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Requester not found"));
        boardService.deleteBoard(boardId, requester);
        return ResponseEntity.noContent().build();
    }

}
