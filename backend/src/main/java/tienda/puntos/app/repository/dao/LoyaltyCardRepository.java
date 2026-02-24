package tienda.puntos.app.repository.dao;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import tienda.puntos.app.repository.entity.LoyaltyCard;

@Repository
@Transactional
public interface LoyaltyCardRepository extends JpaRepository<LoyaltyCard, Long> {

    @Query(value = "SELECT * FROM loyalty_cards WHERE user_id = ?1 AND store_id = ?2", nativeQuery = true)
    Optional<LoyaltyCard> findByUserIdAndStoreId(Long userId, Long storeId);

    @Query(value = "SELECT * FROM loyalty_cards WHERE user_id = ?1", nativeQuery = true)
    List<LoyaltyCard> findByUserId(Long userId);

    @Query(value = "SELECT * FROM loyalty_cards WHERE store_id = ?1", nativeQuery = true)
    List<LoyaltyCard> findByStoreId(Long storeId);

    @Modifying
    @Query(value = "UPDATE `ruta66market`.`loyalty_cards` SET `last_visit` = ?2 WHERE `id` = ?1;", nativeQuery = true)
    void updateLastAccess(Long cardId, Date dateTime);
}