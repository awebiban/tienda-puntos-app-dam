package tienda.puntos.app.services.User;

import tienda.puntos.app.model.dto.UserDTO;
import tienda.puntos.app.repository.entity.User;

public interface UserService {

    UserDTO findByEmail(String email);

    UserDTO save(User user);

}
