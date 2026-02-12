package tienda.puntos.app.services.loyalty;

import java.util.List;
import tienda.puntos.app.model.dto.LoyaltyCardDTO;
import tienda.puntos.app.model.dto.TransactionDTO;

public interface LoyaltyCardService {
    List<LoyaltyCardDTO> getCardsByUser(Long userId);
    LoyaltyCardDTO addPoints(Long userId, Long storeId, int amountSpent);
    LoyaltyCardDTO redeemReward(Long userId, Long storeId, Long rewardId);
    List<TransactionDTO> getHistory(Long cardId);
}