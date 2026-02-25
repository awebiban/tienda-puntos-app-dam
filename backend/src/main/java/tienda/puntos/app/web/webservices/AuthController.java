package tienda.puntos.app.web.webservices;

import java.util.HashMap;
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

            // Usamos HashMap en lugar de Map.of para evitar NullPointerExceptions
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("id", userDTO.getId().toString());
            response.put("email", userDTO.getEmail());
            response.put("nickname", userDTO.getNickname() != null ? userDTO.getNickname() : "Usuario");
            response.put("role", userDTO.getRole().name());

            return ResponseEntity.ok(response);

        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Credenciales inválidas"));
        }
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signup(@RequestBody Map<String, String> signupRequest) {
        String email = signupRequest.get("email");
        String password = signupRequest.get("password");
        // CORRECCIÓN: Capturamos "nickname" que es la clave que manda el Frontend
        String name = signupRequest.get("nickname");

        // 1. Verificación de existencia
        try {
            userService.findByEmail(email);
            return ResponseEntity.badRequest().body(Map.of("error", "El email ya está registrado"));
        } catch (RuntimeException e) {
            // Usuario no existe, procedemos...
        }

        // 2. Mapeo al DTO
        UserDTO newUser = new UserDTO();
        newUser.setEmail(email);
        newUser.setNickname(name != null ? name : "Nuevo Usuario"); // Protección extra
        newUser.setPassword(password);
        newUser.setRole(Role.CLIENTE);

        // 3. Persistencia
        UserDTO savedUser = userService.save(newUser);

        // 4. Token
        String token = jwtService.generateToken(email);

        // 5. Respuesta segura con HashMap (Tolera nulos si los hubiera)
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("id", savedUser.getId().toString());
        response.put("email", savedUser.getEmail());
        response.put("nickname", savedUser.getNickname());
        response.put("role", savedUser.getRole().name());

        return ResponseEntity.ok(response);
    }

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