package tienda.puntos.app.repository.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import tienda.puntos.app.repository.entity.Reward;

@Repository
@Transactional
public interface RewardRepository extends JpaRepository<Reward, Long> {

    @Query(value = "SELECT * FROM rewards WHERE store_id = ?1 AND active = 1", nativeQuery = true)
    List<Reward> findRewardsByStore(Long storeId);

    @Query(value = "UPDATE `ruta66market`.`rewards` SET `active` = 1 WHERE `id` = ?1", nativeQuery = true)
    void activeReward(Long rewardId);

    @Modifying
    @Query(value = "UPDATE `ruta66market`.`rewards` SET `active` = 0 WHERE `id` = ?1", nativeQuery = true)
    void disableReward(Long rewardId);

}
