package tienda.puntos.app.services.user;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import tienda.puntos.app.model.dto.UserDTO;
import tienda.puntos.app.repository.dao.UserRepository;
import tienda.puntos.app.repository.entity.User;
import tienda.puntos.app.utils.Role;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDTO findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(UserDTO::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));
    }

    @Override
    public UserDTO findById(Long userId) {
        return userRepository.findById(userId)
                .map(UserDTO::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + userId));
    }

    @Override
    public UserDTO save(UserDTO userDTO) {
        User userEntity = UserDTO.convertToEntity(userDTO);

        if (userEntity.getId() == null) {
            userEntity.setCreatedAt(LocalDateTime.now());
        }

        if (userDTO.getPassword() != null) {
            userEntity.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        userEntity.setRole(Role.CLIENTE);

        User savedUser = userRepository.save(userEntity);

        return UserDTO.convertToDTO(savedUser);
    }

    @Override
    @Transactional
    public UserDTO update(UserDTO user) {
        User existingUser = this.userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        existingUser.setEmail(user.getEmail());
        existingUser.setNickname(user.getNickname());
        existingUser.setRole(user.getRole());

        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            existingUser.setPassword(this.passwordEncoder.encode(user.getPassword()));
        }

        if (user.getCreatedAt() != null) {
            existingUser.setCreatedAt(user.getCreatedAt());
        }

        return UserDTO.convertToDTO(this.userRepository.save(existingUser));
    }

}