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
        // En lugar de .get(), usamos .map() y manejamos el error si no existe
        return userRepository.findByEmail(email)
                .map(UserDTO::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));
    }

    @Override
    public UserDTO findById(Long userId) {
        // En lugar de .get(), usamos .map() y manejamos el error si no existe
        return userRepository.findById(userId)
                .map(UserDTO::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + userId));
    }

    @Override
    public UserDTO save(UserDTO userDTO) {
        // 1. Convertimos el DTO a Entidad
        User userEntity = UserDTO.convertToEntity(userDTO);

        // 2. Si es un usuario nuevo (id nulo), asignamos la fecha de creación actual
        if (userEntity.getId() == null) {
            userEntity.setCreatedAt(LocalDateTime.now());
        }

        // 3. Encriptamos la contraseña (si no lo estás haciendo ya aquí)
        if (userDTO.getPassword() != null) {
            userEntity.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        userEntity.setRole(Role.CLIENTE);

        // 4. Guardamos en la base de datos
        User savedUser = userRepository.save(userEntity);

        // 5. Devolvemos el DTO
        return UserDTO.convertToDTO(savedUser);
    }

    @Override
    @Transactional
    public UserDTO update(UserDTO user) {
        // 1. Buscamos el usuario real que tiene la contraseña actual
        User existingUser = this.userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Actualizamos campos básicos
        existingUser.setEmail(user.getEmail());
        existingUser.setNickname(user.getNickname());
        existingUser.setRole(user.getRole());

        // 3. LÓGICA DE CONTRASEÑA: Solo si viene algo en el DTO la cambiamos
        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            existingUser.setPassword(this.passwordEncoder.encode(user.getPassword()));
        }
        // Si es null, no tocamos existingUser.getPassword(), manteniendo la anterior

        // 4. LÓGICA DE FECHA: No deberías permitir cambiar la fecha de creación
        // Si el DTO trae null, mantenemos la de la base de datos
        if (user.getCreatedAt() != null) {
            existingUser.setCreatedAt(user.getCreatedAt());
        }

        return UserDTO.convertToDTO(this.userRepository.save(existingUser));
    }

}