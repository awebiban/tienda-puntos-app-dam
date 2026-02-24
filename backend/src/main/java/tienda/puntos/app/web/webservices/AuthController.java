package tienda.puntos.app.web.webservices;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tienda.puntos.app.model.dto.UserDTO;
import tienda.puntos.app.services.auth.JwtService;
import tienda.puntos.app.services.User.UserService;
import tienda.puntos.app.utils.Role;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = { "http://localhost:4200", "https://ruta66.synology.me" })
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    /**
     * Hace login un usuario en el sistema.
     */
    @PostMapping("/log-in")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");

        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password));

            UserDetails springUser = (UserDetails) auth.getPrincipal();
            UserDTO userDTO = userService.findByEmail(springUser.getUsername());
            String token = jwtService.generateToken(email);

            // Devolvemos todos los datos, incluyendo el ID convertido a String
            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "id", userDTO.getId().toString(),
                    "email", userDTO.getEmail(),
                    "nickname", userDTO.getNickname(),
                    "role", userDTO.getRole().name()));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Credenciales inválidas"));
        }
    }

    /**
     * Registra un nuevo usuario en el sistema.
     */
    @PostMapping("/sign-up")
    public ResponseEntity<?> signup(@RequestBody Map<String, String> signupRequest) {
        String email = signupRequest.get("email");
        String password = signupRequest.get("password");
        String name = signupRequest.get("name"); // Leemos "name" que envía Angular

        try {
            userService.findByEmail(email);
            return ResponseEntity.badRequest().body(Map.of("error", "El usuario ya existe"));
        } catch (RuntimeException e) {
            // El email está libre
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(email);
        userDTO.setNickname(name);
        userDTO.setRole(Role.CLIENTE);
        userDTO.setPassword(password); // Se encripta dentro del UserServiceImpl

        // IMPORTANTE: Reasignamos el DTO para capturar el ID autogenerado por la BBDD
        userDTO = userService.save(userDTO);

        String token = jwtService.generateToken(email);

        return ResponseEntity.ok(Map.of(
                "token", token,
                "id", userDTO.getId().toString(),
                "email", userDTO.getEmail(),
                "nickname", userDTO.getNickname(),
                "role", userDTO.getRole().name()));
    }

    /**
     * Devuelve el email y el rol del usuario si el token todavía es válido.
     */
    @GetMapping("/validate")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body(Map.of("error", "No autenticado o expirado"));
        }

        return ResponseEntity.ok(Map.of(
                "email", authentication.getName(),
                "authorities", authentication.getAuthorities().toString()));
    }
}