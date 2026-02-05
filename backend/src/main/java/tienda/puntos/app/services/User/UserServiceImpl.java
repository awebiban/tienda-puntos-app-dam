package tienda.puntos.app.services.User;

import org.springframework.stereotype.Service;

import tienda.puntos.app.model.dto.UserDTO;
import tienda.puntos.app.repository.entity.User;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public UserDTO findByEmail(String email) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByEmail'");
    }

    @Override
    public void save(User user) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

}
