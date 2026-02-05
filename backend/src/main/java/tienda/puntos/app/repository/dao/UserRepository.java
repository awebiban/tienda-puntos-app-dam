package tienda.puntos.app.repository.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import tienda.puntos.app.repository.entity.User;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT * FROM users WHERE email = ?1", nativeQuery = true)
    Optional<User> findByEmail(String email);

}
