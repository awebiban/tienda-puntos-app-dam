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

    @Autowired
    private LoyaltyCardRepository cardRepo;

    @Autowired
    private TransactionRepository transRepo;

    @Autowired
    private StoreRepository storeRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
<<<<<<< Updated upstream
    private RewardRepository rewardRepo;
=======
    private RewardRepository rewardRepository;

    @Override
    public LoyaltyCardDTO getCardById(Long cid) {
        return loyaltyCardRepository.findById(cid)
                .map(LoyaltyCardDTO::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Tarjeta no encontrada"));
    }

    @Override
    public List<LoyaltyCardDTO> getCardsByStore(Long storeId) {
        return loyaltyCardRepository.findByStoreId(storeId).stream()
                .map(LoyaltyCardDTO::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public LoyaltyCardDTO addPointsToCard(Long cardId, Long storeId, Long userId, int points) {
        LoyaltyCard card = loyaltyCardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Tarjeta no encontrada"));

        card.setCurrentBalance(card.getCurrentBalance() + points);
        card.setTotalAccumulated(card.getTotalAccumulated() + points);

        // Registrar la transacción de acumulación
        Transaction t = new Transaction();
        t.setLoyaltyCard(card);
        t.setAmount(points);
        t.setType(TransactionType.EARN);
        t.setCreatedAt(LocalDateTime.now());
        transactionRepository.save(t);

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
>>>>>>> Stashed changes

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

        int pointsToAdd = amountSpent <= 1 ? 0 : Math.round(amountSpent / (float) store.getPointsRatio());

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