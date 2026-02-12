package tienda.puntos.app.services.loyalty;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import tienda.puntos.app.model.dto.LoyaltyCardDTO;
import tienda.puntos.app.model.dto.TransactionDTO;
import tienda.puntos.app.repository.dao.LoyaltyCardRepository;
import tienda.puntos.app.repository.dao.RewardRepository;
import tienda.puntos.app.repository.dao.StoreRepository;
import tienda.puntos.app.repository.dao.TransactionRepository;
import tienda.puntos.app.repository.dao.UserRepository;
import tienda.puntos.app.repository.entity.LoyaltyCard;
import tienda.puntos.app.repository.entity.Reward;
import tienda.puntos.app.repository.entity.Store;
import tienda.puntos.app.repository.entity.Transaction;
import tienda.puntos.app.repository.entity.User;
import tienda.puntos.app.utils.TransactionType;

@Service
public class LoyaltyCardServiceImpl implements LoyaltyCardService {

    @Autowired private LoyaltyCardRepository cardRepo;
    @Autowired private TransactionRepository transRepo;
    @Autowired private StoreRepository storeRepo;
    @Autowired private UserRepository userRepo;
    @Autowired private RewardRepository rewardRepo;

    @Override
    public List<LoyaltyCardDTO> getCardsByUser(Long userId) {
        return cardRepo.findAllByUserId(userId).stream()
                .map(LoyaltyCardDTO::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public LoyaltyCardDTO addPoints(Long userId, Long storeId, int amountSpent) {
        Store store = storeRepo.findById(storeId).orElseThrow(() -> new RuntimeException("Tienda no encontrada"));
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        LoyaltyCard card = cardRepo.findByUserAndStore(userId, storeId)
                .orElseGet(() -> {
                    LoyaltyCard newCard = new LoyaltyCard();
                    newCard.setUser(user);
                    newCard.setStore(store);
                    newCard.setCurrentBalance(0);
                    newCard.setTotalAccumulated(0);
                    return newCard;
                });

        int pointsToAdd = amountSpent * store.getPointsRatio();

        card.setCurrentBalance(card.getCurrentBalance() + pointsToAdd);
        card.setTotalAccumulated(card.getTotalAccumulated() + pointsToAdd);
        card.setLastVisited(LocalDateTime.now());

        LoyaltyCard savedCard = cardRepo.save(card);

        // Crear Transacción
        Transaction trans = new Transaction();
        trans.setLoyaltyCard(savedCard);
        trans.setAmount(pointsToAdd);
        trans.setType(TransactionType.EARN);
        trans.setCreatedAt(LocalDateTime.now());
        transRepo.save(trans);

        return LoyaltyCardDTO.convertToDTO(savedCard);
    }

    @Override
    @Transactional
    public LoyaltyCardDTO redeemReward(Long userId, Long storeId, Long rewardId) {
        LoyaltyCard card = cardRepo.findByUserAndStore(userId, storeId)
                .orElseThrow(() -> new RuntimeException("No tienes puntos en esta tienda"));

        Reward reward = rewardRepo.findById(rewardId).orElseThrow(() -> new RuntimeException("Premio no encontrado"));

        if (card.getCurrentBalance() < reward.getPointsCost()) {
            throw new RuntimeException("Puntos insuficientes para este canje");
        }

        card.setCurrentBalance(card.getCurrentBalance() - reward.getPointsCost());
        LoyaltyCard savedCard = cardRepo.save(card);

        Transaction trans = new Transaction();
        trans.setLoyaltyCard(savedCard);
        trans.setAmount(reward.getPointsCost());
        trans.setType(TransactionType.REDEEM);
        trans.setCreatedAt(LocalDateTime.now());
        transRepo.save(trans);

        return LoyaltyCardDTO.convertToDTO(savedCard);
    }

    @Override
    public List<TransactionDTO> getHistory(Long cardId) {
        return transRepo.findByLoyaltyCardId(cardId).stream()
                .map(TransactionDTO::convertToDTO)
                .collect(Collectors.toList());
    }
}