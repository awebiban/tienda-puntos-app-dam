package tienda.puntos.app.services.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import tienda.puntos.app.repository.dao.UserRepository;
import tienda.puntos.app.repository.entity.User;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("====== INTENTO DE LOGIN ======");
        System.out.println("1. Buscando email: " + email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    System.out.println("❌ ERROR: El email no existe en la BBDD.");
                    return new UsernameNotFoundException("Usuario no encontrado con email: " + email);
                });

        System.out.println("2. ¡Usuario encontrado!: " + user.getNickname());
        System.out.println("3. Hash guardado en BBDD: " + user.getPassword());
        System.out.println("==============================");

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }
}