package tienda.puntos.app.services.loyalty;

import java.util.List;
import tienda.puntos.app.model.dto.LoyaltyCardDTO;
import tienda.puntos.app.model.dto.TransactionDTO;

public interface LoyaltyCardService {
    List<LoyaltyCardDTO> getCardsByUser(Long userId);
<<<<<<< Updated upstream
=======

    List<TransactionDTO> getHistory(Long cardId);

    LoyaltyCardDTO createCard(Long userId, Long storeId);

    LoyaltyCardDTO redeemReward(Long userId, Long storeId, Long rewardId);

    // Métodos para el flujo del Vendedor (Merchant)
    List<LoyaltyCardDTO> getCardsByStore(Long storeId);

    LoyaltyCardDTO addPointsToCard(Long cardId, Long storeId, Long userId, int points);

>>>>>>> Stashed changes
    LoyaltyCardDTO addPoints(Long userId, Long storeId, int amountSpent);
    LoyaltyCardDTO redeemReward(Long userId, Long storeId, Long rewardId);
    List<TransactionDTO> getHistory(Long cardId);
}