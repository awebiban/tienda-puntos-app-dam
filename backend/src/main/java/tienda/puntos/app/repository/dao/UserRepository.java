package tienda.puntos.app.repository.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import tienda.puntos.app.repository.entity.User;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

}
