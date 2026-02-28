package tienda.puntos.app.services.loyalty;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tienda.puntos.app.model.dto.LoyaltyCardDTO;
import tienda.puntos.app.model.dto.TransactionDTO;
import tienda.puntos.app.repository.dao.LoyaltyCardRepository;
import tienda.puntos.app.repository.dao.RewardRepository;
import tienda.puntos.app.repository.dao.StoreRepository;
import tienda.puntos.app.repository.dao.TransactionRepository;
import tienda.puntos.app.repository.dao.UserRepository;
import tienda.puntos.app.repository.entity.LoyaltyCard;
import tienda.puntos.app.repository.entity.Reward;
import tienda.puntos.app.repository.entity.Transaction;
import tienda.puntos.app.utils.TransactionType;

@Service
public class LoyaltyCardServiceImpl implements LoyaltyCardService {

    @Autowired
    private LoyaltyCardRepository loyaltyCardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private RewardRepository rewardRepository;

    @Override
    public LoyaltyCardDTO getCardById(Long cid) {
        return loyaltyCardRepository.findById(cid)
                .map(LoyaltyCardDTO::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Tarjeta no encontrada"));
    }

    @Override
    public LoyaltyCardDTO updateCard(Long cid, LoyaltyCardDTO cardDTO) {
        LoyaltyCard existingCard = loyaltyCardRepository.findById(cid)
                .orElseThrow(() -> new RuntimeException("Error: Tarjeta de fidelidad no encontrada con ID: " + cid));

        existingCard.setCurrentBalance(cardDTO.getCurrentBalance());

        // Asignar puntos
        existingCard.setTotalAccumulated(cardDTO.getTotalAccumulated());

        // Actualizamos la fecha de última visita si el Merchant está realizando una
        // acción ahora
        existingCard.setLastVisited(java.time.LocalDateTime.now());

        LoyaltyCard savedCard = loyaltyCardRepository.save(existingCard);
        return LoyaltyCardDTO.convertToDTO(savedCard);
    }

    @Override
    public List<LoyaltyCardDTO> getCardsByStore(Long storeId) {
        System.out.println(">>> [Merchant Hub] Iniciando consulta para Store ID: " + storeId);

        List<LoyaltyCard> cards = loyaltyCardRepository.findByStoreId(storeId);

        if (cards.isEmpty()) {
            System.out.println(">>> [Aviso] No se encontraron tarjetas para esta tienda.");
        } else {
            System.out.println(">>> [Listado de Tarjetas Encontradas]:");
            // Imprimimos cada tarjeta con un formato legible
            cards.forEach(card -> System.out.println(card.getUser()));
        }

        return cards.stream()
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

    @Override
    public List<LoyaltyCardDTO> getCardsByUser(Long userId) {
        return loyaltyCardRepository.findByUserId(userId).stream()
                .map(LoyaltyCardDTO::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionDTO> getHistory(Long cardId) {
        return transactionRepository.findByLoyaltyCardId(cardId).stream()
                .map(TransactionDTO::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public LoyaltyCardDTO redeemReward(Long userId, Long storeId, Long rewardId) {
        // 1. Buscamos la tarjeta y el premio
        LoyaltyCard card = loyaltyCardRepository.findByUserIdAndStoreId(userId, storeId)
                .orElseThrow(() -> new RuntimeException("Tarjeta de fidelidad no encontrada"));

        Reward reward = rewardRepository.findById(rewardId)
                .orElseThrow(() -> new RuntimeException("Premio no encontrado"));

        // 2. Verificamos saldo
        if (card.getCurrentBalance() < reward.getPointsCost()) {
            throw new RuntimeException("Saldo insuficiente para canjear este premio");
        }

        // 3. Restamos puntos
        card.setCurrentBalance(card.getCurrentBalance() - reward.getPointsCost());

        // 4. Registramos la transacción de canje (negativa)
        Transaction t = new Transaction();
        t.setLoyaltyCard(card);
        t.setAmount(-reward.getPointsCost());
        t.setType(TransactionType.REDEEM);
        t.setCreatedAt(LocalDateTime.now());
        transactionRepository.save(t);

        return LoyaltyCardDTO.convertToDTO(loyaltyCardRepository.save(card));
    }

    @Override
    public void updateLastAccess(Long cardId) {
        Date dateTime = new Date();
        loyaltyCardRepository.updateLastAccess(cardId, dateTime);
    }

    @Override
    @Deprecated
    public LoyaltyCardDTO addPoints(Long userId, Long storeId, int amountSpent) {
        return null;
    }
}