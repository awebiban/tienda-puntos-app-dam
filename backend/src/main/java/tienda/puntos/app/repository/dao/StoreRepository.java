package tienda.puntos.app.repository.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import tienda.puntos.app.repository.entity.Store;

@Repository
@Transactional
public interface StoreRepository extends JpaRepository<Store, Long> {

    @Query(value = "SELECT * FROM `ruta66market`.`stores` WHERE `company_id` = ?1 ORDER BY `id`", nativeQuery = true)
    List<Store> findAllByCompanyId(Long companyId);

    @Query(value = "UPDATE `ruta66market`.`stores` SET `is_visible` = 0 WHERE `id` = ?1;", nativeQuery = true)
    void disable(Long storeId);

    @Query(value = "UPDATE `ruta66market`.`stores` SET `is_visible` = 1 WHERE `id` = ?1;", nativeQuery = true)
    void activate(Long storeId);

}
