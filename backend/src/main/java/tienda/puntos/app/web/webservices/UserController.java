package tienda.puntos.app.web.webservices;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import tienda.puntos.app.model.dto.UserDTO;
import tienda.puntos.app.services.user.UserService;
import tienda.puntos.app.utils.Views;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{userId}")
    @JsonView(Views.Detalle.class)
    public ResponseEntity<UserDTO> findById(@PathVariable("userId") Long uid) {
        return ResponseEntity.ok(this.userService.findById(uid));
    }

    @PostMapping("/email")
    @JsonView(Views.Detalle.class)
    public ResponseEntity<UserDTO> findByEmail(@RequestBody Map<String, String> payload) {
        // Extraemos el valor asociado a la clave "email" del JSON
        String email = payload.get("email");

        // Si el email es nulo en el JSON, lanzamos un error controlado
        if (email == null) {
            return ResponseEntity.unprocessableContent().build();
        }

        return ResponseEntity.ok(this.userService.findByEmail(email));
    }

    @PostMapping("/save")
    @JsonView(Views.Detalle.class)
    public ResponseEntity<UserDTO> save(@RequestBody UserDTO user) {
        return ResponseEntity.ok(this.userService.save(user));
    }

    @PutMapping("/update")
    @JsonView(Views.Detalle.class)
    public ResponseEntity<UserDTO> update(@RequestBody UserDTO user) { // Se pasa el id al JPA para que busque el
                                                                       // registro necesario
        // y sustituya
        return ResponseEntity.ok(this.userService.update(user));
    }
}
