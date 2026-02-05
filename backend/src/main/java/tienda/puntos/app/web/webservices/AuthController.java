package tienda.puntos.app.web.webservices;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tienda.puntos.app.model.dto.UserDTO;
import tienda.puntos.app.repository.entity.User;
import tienda.puntos.app.services.User.UserService;
import tienda.puntos.app.services.auth.JwtService;
import tienda.puntos.app.utils.Role;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = { "http://localhost:4200",
        "https://ruta66.synology.me" })
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    /**
     * Hace login un usuario en el sistema.
     *
     * @param loginRequest JSON que contiene la información básica del
     *                     usuario:<br>
     *                     - email (String): Correo electrónico único del
     *                     usuario.<br>
     *                     - password (String): Contraseña en texto plano que
     *                     será encriptada antes de comprobar.
     *
     * @return ResponseEntity con:<br>
     *         - 200 (Ok) y inicia sesión.<br>
     *         - 401 (Unautorized) si las credenciales son erroneas.<br>
     */
    @PostMapping("/log-in")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");

        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password));

            User user = (User) auth.getPrincipal();
            String token = jwtService.generateToken(email);

            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "email", user.getEmail(),
                    "nickname", user.getNickname(),
                    "role", user.getRole().name()));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Credenciales inválidas"));
        }
    }

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param registerRequest JSON que contiene la información básica del
     *                        usuario:<br>
     *                        - email (String): Correo electrónico único del
     *                        usuario.<br>
     *                        - name (String): Nombre del usuario.<br>
     *                        - password (String): Contraseña en texto plano que
     *                        será encriptada antes de guardar.
     *
     * @return ResponseEntity con:<br>
     *         - 200 (Ok) y los datos del usuario si se registra correctamente.<br>
     *         - 400 (Bad Request) si el email ya está en uso.
     */
    @PostMapping("/sign-up")
    public ResponseEntity<?> signup(@RequestBody Map<String, String> signupRequest) {
        String email = signupRequest.get("email");
        String password = signupRequest.get("password");
        String name = signupRequest.get("nickname");

        if (userService.findByEmail(email) != null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Usuario ya existe"));
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(email);
        userDTO.setNickname(name);
        userDTO.setRole(Role.CLIENTE);
        userDTO.setPassword(this.passwordEncoder.encode(password));

        userService.save(userDTO);

        String token = jwtService.generateToken(email);

        return ResponseEntity.ok(Map.of(
                "token", token,
                "email", userDTO.getEmail(),
                "nickname", userDTO.getNickname(),
                "role", userDTO.getRole().name()));
    }

    /**
     * Devulve el email y el rol del usuario si el token todavia es Valido o es
     * Correcto.
     *
     * @param Authentication (JSON) que contiene la información básica del usuario:
     *                       - Bearer token: "userToken".
     *
     * @return ResponseEntity con:<br>
     *         - 200 (Ok) email del usuario y su rol.<br>
     *         - 401 (Unauthorized) si el token no es correcto o esta caducado.
     */
    @GetMapping("/validate")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body(Map.of("error", "No autenticado o expirado"));
        }

        return ResponseEntity.ok(Map.of(
                "email", authentication.getName(),
                "authorities", authentication.getAuthorities()));
    }
}
