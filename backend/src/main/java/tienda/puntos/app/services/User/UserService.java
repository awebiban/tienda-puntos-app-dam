package tienda.puntos.app.services.User;

import tienda.puntos.app.model.dto.UserDTO;

public interface UserService {

    UserDTO findByEmail(String email);

    UserDTO save(UserDTO user);

    UserDTO update(UserDTO user);

}
