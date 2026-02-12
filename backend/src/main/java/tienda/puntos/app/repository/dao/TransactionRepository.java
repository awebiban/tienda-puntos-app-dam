package tienda.puntos.app.repository.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import tienda.puntos.app.repository.entity.Transaction;

@Repository
@Transactional
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query(value = "SELECT * FROM transactions WHERE card_id = ?1 ORDER BY created_at DESC", nativeQuery = true)
    List<Transaction> findByLoyaltyCardId(Long cardId);
}