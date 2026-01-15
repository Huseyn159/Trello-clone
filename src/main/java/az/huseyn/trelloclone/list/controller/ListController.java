package az.huseyn.trelloclone.list.controller;


import az.huseyn.trelloclone.card.dto.CardResponseDto;
import az.huseyn.trelloclone.card.repository.CardRepository;
import az.huseyn.trelloclone.card.service.CardService;
import az.huseyn.trelloclone.list.dto.ListCreateRequestDto;
import az.huseyn.trelloclone.list.dto.ListReorderRequestDto;
import az.huseyn.trelloclone.list.dto.ListResponseDto;
import az.huseyn.trelloclone.list.service.ListService;
import az.huseyn.trelloclone.user.entity.UserEntity;
import az.huseyn.trelloclone.user.exception.UserNotFoundException;
import az.huseyn.trelloclone.user.repository.UserRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.catalina.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "List", description = "List (column) operations")
@RestController
@RequestMapping("/api/lists")
public class ListController {

    private final UserRepository userRepository;
    private final ListService listService;
    private final CardService cardService;

    public ListController(UserRepository userRepository, ListService listService, CardService cardService) {
        this.userRepository = userRepository;
        this.listService = listService;
        this.cardService = cardService;
    }


    @PostMapping
    public ResponseEntity<ListResponseDto> createList(
            @Valid @RequestBody ListCreateRequestDto listCreateRequestDto,
            @RequestParam Long userId) {


        UserEntity requester = userRepository.findById(userId).orElseThrow(()->
                new UserNotFoundException("User not found"));
        ListResponseDto listResponseDto = listService.createList(listCreateRequestDto,requester);
        return ResponseEntity.status(HttpStatus.CREATED).body(listResponseDto);

    }

    @GetMapping("/{listId}/cards")
    public ResponseEntity<List<CardResponseDto>> getCardsByList(
            @PathVariable Long listId
    ) {
        return ResponseEntity.ok(
                cardService.getCardsByList(listId)
        );
    }
    @PatchMapping("/reorder")
    public ResponseEntity<Void> reorderList(
            @RequestBody ListReorderRequestDto dto,
            @RequestParam Long userId
    ) {
        listService.reorderList(dto);
        return ResponseEntity.noContent().build();
    }

    @Transactional
    @DeleteMapping("/{listId}")
    public ResponseEntity<Void> deleteList(
            @PathVariable Long listId,
            @RequestParam Long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(()->
                new UserNotFoundException("User not found"));
        listService.deleteList(listId,user);
        return ResponseEntity.noContent().build();

    }




}
