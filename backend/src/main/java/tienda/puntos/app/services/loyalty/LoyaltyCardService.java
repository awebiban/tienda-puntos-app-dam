package tienda.puntos.app.services.loyalty;

import java.util.List;

import tienda.puntos.app.model.dto.LoyaltyCardDTO;
import tienda.puntos.app.model.dto.TransactionDTO;

public interface LoyaltyCardService {
    // Métodos para el flujo del Cliente
    LoyaltyCardDTO getCardById(Long cid);

    List<LoyaltyCardDTO> getCardsByUser(Long userId);

    List<TransactionDTO> getHistory(Long cardId);

    LoyaltyCardDTO createCard(Long userId, Long storeId);

    LoyaltyCardDTO redeemReward(Long userId, Long storeId, Long rewardId);

    // Métodos para el flujo del Vendedor (Merchant)
    List<LoyaltyCardDTO> getCardsByStore(Long storeId);

    LoyaltyCardDTO addPointsToCard(Long cardId, int points);

    LoyaltyCardDTO addPoints(Long userId, Long storeId, int amountSpent);

    // Método para actualizar la fecha de último acceso (nuevo)
    void updateLastAccess(Long cardId);
}