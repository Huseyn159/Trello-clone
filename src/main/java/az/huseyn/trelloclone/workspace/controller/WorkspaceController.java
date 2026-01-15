package az.huseyn.trelloclone.workspace.controller;

import az.huseyn.trelloclone.board.dto.BoardResponseDto;
import az.huseyn.trelloclone.board.service.BoardService;
import az.huseyn.trelloclone.user.entity.UserEntity;
import az.huseyn.trelloclone.user.exception.UserNotFoundException;
import az.huseyn.trelloclone.user.repository.UserRepository;
import az.huseyn.trelloclone.workspace.dto.AddWorkspaceMemberRequestDto;
import az.huseyn.trelloclone.workspace.dto.WorkspaceCreateRequestDto;
import az.huseyn.trelloclone.workspace.dto.WorkspaceResponseDto;
import az.huseyn.trelloclone.workspace.service.WorkspaceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Workspace", description = "Workspace & members management")
@RestController
@RequestMapping("/api/workspaces")
public class WorkspaceController {
    private final BoardService boardService;
    private final WorkspaceService workspaceService;
    private final UserRepository userRepository;

    public WorkspaceController(WorkspaceService workspaceService, BoardService boardService, UserRepository userRepository) {
        this.workspaceService = workspaceService;
        this.boardService = boardService;
        this.userRepository = userRepository;
    }

    @PostMapping("/createWorkspace")
    public ResponseEntity<WorkspaceResponseDto> createWorkspace(
            @Valid @RequestBody WorkspaceCreateRequestDto dto,
            @RequestParam Long creatorId
    ) {
        UserEntity creator = userRepository.getById(creatorId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(workspaceService.createWorkspace(dto, creator));
    }
    @GetMapping
    public ResponseEntity<List<WorkspaceResponseDto>> getUserWorkspaces(
            @RequestParam Long userId
    ) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return ResponseEntity.ok(
                workspaceService.getWorkspacesByUser(user)
        );
    }
    @GetMapping("/{workspaceId}/boards")
    public ResponseEntity<List<BoardResponseDto>> getBoards(
            @PathVariable Long workspaceId) {

        List<BoardResponseDto> boards =
                boardService.getBoardsByWorkspace(workspaceId);

        return ResponseEntity.ok(boards);
    }

    @PostMapping("/{workspaceId}/members")
    public ResponseEntity<Void> addMember(
            @PathVariable Long workspaceId,
            @RequestParam Long requesterId,
            @RequestBody AddWorkspaceMemberRequestDto request
    ) {

        UserEntity requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new RuntimeException("Requester not found"));

        workspaceService.addMember(
                workspaceId,
                request.getUserId(),
                requester
        );

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{workspaceId}")
    public ResponseEntity<Void> deleteWorkspace(@PathVariable Long workspaceId,
                                                @RequestParam Long requesterId) {
        UserEntity requester = userRepository.findById(requesterId).orElseThrow(()->
                new UserNotFoundException("User not found"));

        workspaceService.removeWorkspace(workspaceId, requester);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{workspaceId}/members/{memberId}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long workspaceId,
                                             @PathVariable Long memberId,
                                             @RequestParam Long requesterId) {
        UserEntity requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        workspaceService.removeMember(workspaceId, memberId, requester);
        return ResponseEntity.noContent().build();
    }



}
