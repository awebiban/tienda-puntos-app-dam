package tienda.puntos.app.repository.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import tienda.puntos.app.repository.entity.Company;

@Repository
@Transactional
public interface CompanyRepository extends JpaRepository<Company, Long> {

    @Query(value = "SELECT * FROM companies WHERE owner_id = ?1", nativeQuery = true)
    Optional<Company> findCompanyFromUserID(Long userId);

    @Query(value = "SELECT * FROM companies WHERE tax_id = ?1", nativeQuery = true)
    Optional<Company> findCompanyByCIF(String cif);

}
