package az.huseyn.trelloclone.card.service;

import az.huseyn.trelloclone.card.dto.CardCreateRequestDto;
import az.huseyn.trelloclone.card.dto.CardReorderRequestDto;
import az.huseyn.trelloclone.card.dto.CardResponseDto;
import az.huseyn.trelloclone.user.entity.UserEntity;

import java.util.List;

public interface CardService {

    List<CardResponseDto> getCardsByList(Long listId);
    CardResponseDto createCard(CardCreateRequestDto cardCreateRequestDto,UserEntity user);
    void reorderCard(Long cardId, int newPosition,UserEntity user);
    void moveCard(Long cardId, Long targetListId, int newPosition, UserEntity user);
    void deleteCard(Long cardId,UserEntity user);
}
