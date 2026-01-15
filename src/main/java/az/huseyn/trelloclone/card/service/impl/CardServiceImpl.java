package az.huseyn.trelloclone.card.service.impl;

import az.huseyn.trelloclone.card.dto.CardCreateRequestDto;
import az.huseyn.trelloclone.card.dto.CardResponseDto;
import az.huseyn.trelloclone.card.entity.CardEntity;
import az.huseyn.trelloclone.card.exception.CardNotFoundException;
import az.huseyn.trelloclone.card.repository.CardRepository;
import az.huseyn.trelloclone.card.service.CardService;
import az.huseyn.trelloclone.list.entity.ListEntity;
import az.huseyn.trelloclone.list.exception.InvalidPositionException;
import az.huseyn.trelloclone.list.exception.ListNotFoundException;
import az.huseyn.trelloclone.list.repository.ListRepository;
import az.huseyn.trelloclone.user.entity.UserEntity;
import az.huseyn.trelloclone.workspace.service.WorkspacePermissionService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;
    private final ListRepository listRepository;
    private final WorkspacePermissionService workspacePermissionService;

    public CardServiceImpl(CardRepository cardRepository, ListRepository listRepository, WorkspacePermissionService workspacePermissionService) {
        this.cardRepository = cardRepository;
        this.listRepository = listRepository;
        this.workspacePermissionService = workspacePermissionService;
    }

    @Override
    public List<CardResponseDto> getCardsByList(Long listId) {
        ListEntity listEntity = listRepository.findById(listId).orElseThrow(()-> new ListNotFoundException("List not found"));
        List<CardEntity> cards = cardRepository.findByListEntityOrderByPositionAsc(listEntity);
        List<CardResponseDto> cardResponseDtoList = new ArrayList<>();
        for (CardEntity cardEntity : cards) {
            CardResponseDto cardResponseDto = new CardResponseDto();
            cardResponseDto.setCardId(cardEntity.getId());
            cardResponseDto.setCardName(cardEntity.getCardName());
            cardResponseDto.setPosition(cardEntity.getPosition());
            cardResponseDtoList.add(cardResponseDto);
        }
        return cardResponseDtoList;
    }

    @Transactional
    @Override
    public CardResponseDto createCard(CardCreateRequestDto cardCreateRequestDto,UserEntity user) {
        ListEntity listEntity = listRepository.findById(cardCreateRequestDto.getListId())
                .orElseThrow(() -> new ListNotFoundException("List not found"));
        workspacePermissionService.checkCanManageCards(user,listEntity.getBoard().getWorkspace());
        int maxPosition = cardRepository.countByListEntity(listEntity);

        CardEntity cardEntity = new CardEntity();
        cardEntity.setCardName(cardCreateRequestDto.getCardName());
        cardEntity.setCreatedAt(LocalDateTime.now());
        cardEntity.setListEntity(listEntity);
        cardEntity.setPosition(maxPosition + 1);

        cardRepository.save(cardEntity);

        CardResponseDto dto = new CardResponseDto();
        dto.setCardId(cardEntity.getId());
        dto.setCardName(cardEntity.getCardName());
        dto.setPosition(cardEntity.getPosition());
        dto.setCreatedAt(cardEntity.getCreatedAt());
        dto.setListId(listEntity.getId());

        return dto;
    }

    @Transactional
    @Override
    public void reorderCard(Long cardId, int newPosition, UserEntity user) {
        CardEntity cardEntity = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException("Card not found"));

        int oldPosition = cardEntity.getPosition();
        ListEntity list = cardEntity.getListEntity();

        workspacePermissionService.checkCanManageCards(user, list.getBoard().getWorkspace());

        int maxPosition = cardRepository.countByListEntity(list);

        if (newPosition == oldPosition) return;

        if (newPosition < 1 || newPosition > maxPosition) {
            throw new InvalidPositionException("Position must be between 1 and " + maxPosition);
        }

        if (newPosition < oldPosition) {
            cardRepository.increasePosition(list, newPosition, oldPosition, cardId);
        } else {
            cardRepository.decreasePosition(list, oldPosition, newPosition, cardId);
        }

        cardEntity.setPosition(newPosition);
        cardRepository.save(cardEntity);
    }

    @Transactional
    @Override
    public void moveCard(Long cardId, Long targetListId, int newPosition, UserEntity user) {
        CardEntity cardEntity = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException("Card not found"));

        ListEntity sourceList = cardEntity.getListEntity();
        workspacePermissionService.checkCanManageCards(user, sourceList.getBoard().getWorkspace());

        if (sourceList.getId().equals(targetListId)) {
            reorderCard(cardId, newPosition, user);
            return;
        }

        int oldPosition = cardEntity.getPosition();

        ListEntity targetList = listRepository.findById(targetListId)
                .orElseThrow(() -> new ListNotFoundException("List not found"));

        int sourceMax = cardRepository.countByListEntity(sourceList);
        int targetMax = cardRepository.countByListEntity(targetList);

        if (newPosition < 1 || newPosition > targetMax + 1) {
            throw new InvalidPositionException("Position must be between 1 and " + (targetMax + 1));
        }

        cardRepository.decreasePosition(sourceList, oldPosition, sourceMax, cardId);

        cardRepository.increasePosition(targetList, newPosition, targetMax + 1, cardId);

        cardEntity.setListEntity(targetList);
        cardEntity.setPosition(newPosition);
        cardRepository.save(cardEntity);
    }

    @Transactional
    @Override
    public void deleteCard(Long cardId, UserEntity user) {
        CardEntity card = cardRepository.findById(cardId).orElseThrow(() -> new CardNotFoundException("Card not found"));
        ListEntity list = card.getListEntity();
        workspacePermissionService.checkCanManageCards(user,list.getBoard().getWorkspace());
        int oldPosition = card.getPosition();
        int maxPosition = cardRepository.countByListEntity(list);
        cardRepository.decreasePosition(list,oldPosition,maxPosition,cardId);
        cardRepository.delete(card);
    }
}
