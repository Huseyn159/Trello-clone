package az.huseyn.trelloclone.card.controller;

import az.huseyn.trelloclone.card.dto.CardCreateRequestDto;
import az.huseyn.trelloclone.card.dto.CardReorderRequestDto;
import az.huseyn.trelloclone.card.dto.CardResponseDto;
import az.huseyn.trelloclone.card.dto.CardToListReorderDto;
import az.huseyn.trelloclone.card.service.CardService;
import az.huseyn.trelloclone.user.entity.UserEntity;
import az.huseyn.trelloclone.user.exception.UserNotFoundException;
import az.huseyn.trelloclone.user.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Card", description = "Card operations (create, move, reorder, delete)")
@RestController
@RequestMapping("/api/cards")
public class CardController {

    private final CardService cardService;
    private final UserRepository userRepository;

    public CardController(CardService cardService, UserRepository userRepository) {
        this.cardService = cardService;
        this.userRepository = userRepository;
    }

    @Operation(summary = "Create new card")
    @PostMapping
    public ResponseEntity<CardResponseDto> createCard(
            @Valid @RequestBody CardCreateRequestDto dto,
            @RequestParam Long userId
    ) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        CardResponseDto response = cardService.createCard(dto, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Reorder cards in same list")
    @PatchMapping("/reorder")
    public ResponseEntity<Void> reorderCard(
            @Valid @RequestBody CardReorderRequestDto dto,
            @RequestParam Long userId
    ) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        cardService.reorderCard(
                dto.getCardId(),
                dto.getNewPosition(),
                user
        );

        return ResponseEntity.noContent().build(); // 204
    }

    @Operation(summary = "Move card between lists")
    @PatchMapping("/move")
    public ResponseEntity<Void> moveCard(
            @Valid @RequestBody CardToListReorderDto dto,
            @RequestParam Long userId
    ) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        cardService.moveCard(
                dto.getCardId(),
                dto.getTargetListId(),
                dto.getNewPosition(),
                user
        );

        return ResponseEntity.noContent().build(); // 204
    }

    @Operation(summary = "Delete Card")
    @DeleteMapping("/{cardId}")
    public ResponseEntity<Void> deleteCard(
            @PathVariable Long cardId,
            @RequestParam Long userId
    ){
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        cardService.deleteCard(cardId,user);
        return ResponseEntity.noContent().build();
    }
}