package tienda.puntos.app.repository.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import tienda.puntos.app.repository.entity.LoyaltyCard;

@Repository
@Transactional
public interface LoyaltyCardRepository extends JpaRepository<LoyaltyCard, Long> {

    @Query(value = "SELECT * FROM loyalty_cards WHERE user_id = ?1 AND store_id = ?2", nativeQuery = true)
    Optional<LoyaltyCard> findByUserAndStore(Long userId, Long storeId);

    @Query(value = "SELECT * FROM loyalty_cards WHERE user_id = ?1", nativeQuery = true)
    List<LoyaltyCard> findAllByUserId(Long userId);
}