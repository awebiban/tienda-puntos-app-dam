package tienda.puntos.app.services.loyalty;

import java.util.List;

import tienda.puntos.app.model.dto.LoyaltyCardDTO;
import tienda.puntos.app.model.dto.TransactionDTO;

public interface LoyaltyCardService {
    LoyaltyCardDTO getCardById(Long cid);

    List<LoyaltyCardDTO> getCardsByUser(Long userId);

    List<TransactionDTO> getHistory(Long cardId);

    LoyaltyCardDTO createCard(Long userId, Long storeId);

    LoyaltyCardDTO redeemReward(Long userId, Long storeId, Long rewardId);

    List<LoyaltyCardDTO> getCardsByStore(Long storeId);

    LoyaltyCardDTO addPointsToCard(Long cardId, int points);

    LoyaltyCardDTO addPoints(Long userId, Long storeId, int amountSpent);

    void updateLastAccess(Long cardId);

    LoyaltyCardDTO updateCard(Long cid, LoyaltyCardDTO cardDTO);
}