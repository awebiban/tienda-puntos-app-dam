package tienda.puntos.app.services.loyalty;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tienda.puntos.app.model.dto.LoyaltyCardDTO;
import tienda.puntos.app.repository.dao.LoyaltyCardRepository;
import tienda.puntos.app.repository.dao.UserRepository;
import tienda.puntos.app.repository.dao.StoreRepository;
import tienda.puntos.app.repository.entity.LoyaltyCard;

@Service
public class LoyaltyCardServiceImpl implements LoyaltyCardService {

    @Autowired
    private LoyaltyCardRepository loyaltyCardRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StoreRepository storeRepository;

    @Override
    public List<LoyaltyCardDTO> getCardsByStore(Long storeId) {
        return loyaltyCardRepository.findByStoreId(storeId).stream()
                .map(LoyaltyCardDTO::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public LoyaltyCardDTO addPointsToCard(Long cardId, int points) {
        LoyaltyCard card = loyaltyCardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Tarjeta no encontrada"));
        card.setCurrentBalance(card.getCurrentBalance() + points);
        card.setTotalAccumulated(card.getTotalAccumulated() + points);
        return LoyaltyCardDTO.convertToDTO(loyaltyCardRepository.save(card));
    }

    @Override
    @Transactional
    public LoyaltyCardDTO createCard(Long userId, Long storeId) {
        return loyaltyCardRepository.findByUserIdAndStoreId(userId, storeId)
                .map(LoyaltyCardDTO::convertToDTO)
                .orElseGet(() -> {
                    LoyaltyCard newCard = new LoyaltyCard();
                    newCard.setUser(userRepository.findById(userId).orElseThrow());
                    newCard.setStore(storeRepository.findById(storeId).orElseThrow());
                    newCard.setCurrentBalance(0);
                    newCard.setTotalAccumulated(0);
                    return LoyaltyCardDTO.convertToDTO(loyaltyCardRepository.save(newCard));
                });
    }

    @Override public List<LoyaltyCardDTO> getCardsByUser(Long userId) { return loyaltyCardRepository.findByUserId(userId).stream().map(LoyaltyCardDTO::convertToDTO).collect(Collectors.toList()); }
    @Override public LoyaltyCardDTO addPoints(Long u, Long s, int a) { return null; } // Obsoleto por addPointsToCard
    @Override public List<tienda.puntos.app.model.dto.TransactionDTO> getHistory(Long c) { return List.of(); }
    @Override public LoyaltyCardDTO redeemReward(Long u, Long s, Long r) { return null; }
}