package tienda.puntos.app.repository.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import tienda.puntos.app.repository.entity.Plan;

@Repository
@Transactional
public interface PlanRepository extends JpaRepository<Plan, Long> {

    @Query(value = "UPDATE plans SET active = 0 WHERE id = ?1", nativeQuery = true)
    void disable(int planId);

    @Query(value = "UPDATE plans SET active = 1 WHERE id = ?1", nativeQuery = true)
    void active(int planId);

}
