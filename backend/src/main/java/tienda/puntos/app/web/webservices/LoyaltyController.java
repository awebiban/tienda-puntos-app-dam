package tienda.puntos.app.web.webservices;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import tienda.puntos.app.model.dto.LoyaltyCardDTO;
import tienda.puntos.app.model.dto.TransactionDTO;
import tienda.puntos.app.services.loyalty.LoyaltyCardService;
import tienda.puntos.app.utils.Views;

@RestController
@RequestMapping("/api/loyalty")
@CrossOrigin(origins = { "http://localhost:4200", "https://ruta66.synology.me" })
public class LoyaltyController {

    @Autowired
    private LoyaltyCardService loyaltyService;

    @GetMapping("/{cardId}")
    public ResponseEntity<LoyaltyCardDTO> getCardById(@PathVariable("cardId") Long cid) {
        return ResponseEntity.ok(loyaltyService.getCardById(cid));
    }

    /**
     * Devuelve las tarjetas de un cliente específico.
     */
    @GetMapping("/user/{userId}")
    @JsonView(Views.Resumen.class)
    public ResponseEntity<List<LoyaltyCardDTO>> getMyCards(@PathVariable("userId") Long uid) {
        return ResponseEntity.ok(loyaltyService.getCardsByUser(uid));
    }

    /**
     * NUEVO: Devuelve todas las tarjetas de una tienda (para el panel del
     * vendedor).
     */
    @GetMapping("/store/{storeId}")
    @JsonView(Views.Resumen.class)
    public ResponseEntity<List<LoyaltyCardDTO>> getCardsByStore(@PathVariable("storeId") Long sid) {
        return ResponseEntity.ok(loyaltyService.getCardsByStore(sid));
    }

    /**
     * NUEVO: Crea una tarjeta de fidelidad cuando un usuario se une desde la
     * landing.
     */
    @PostMapping("/join")
    public ResponseEntity<LoyaltyCardDTO> joinStore(@RequestBody Map<String, Long> payload) {
        return ResponseEntity.ok(loyaltyService.createCard(
                payload.get("userId"),
                payload.get("storeId")));
    }

    @GetMapping("/history/{cardId}")
    @JsonView(Views.Detalle.class)
    public ResponseEntity<List<TransactionDTO>> getHistory(@PathVariable("cardId") Long cid) {
        return ResponseEntity.ok(loyaltyService.getHistory(cid));
    }

    /**
     * ACTUALIZADO: Suma puntos.
     * He simplificado para que acepte tanto (userId + storeId) como (cardId).
     */
    @PostMapping("/add-points")
    @JsonView(Views.Detalle.class)
    public ResponseEntity<LoyaltyCardDTO> addPoints(@RequestBody Map<String, Object> payload) {
        // Si el frontend envía cardId directamente (más fácil para el Merchant
        // Dashboard)
        if (payload.containsKey("cardId")) {
            Long cardId = Long.valueOf(payload.get("cardId").toString());
            int points = Integer.parseInt(payload.get("points").toString());
            return ResponseEntity.ok(loyaltyService.addPointsToCard(cardId, points));
        }

        // Si el frontend envía userId y storeId (como tenías antes)
        Long userId = Long.valueOf(payload.get("userId").toString());
        Long storeId = Long.valueOf(payload.get("storeId").toString());
        int amount = Integer.parseInt(payload.get("amount").toString());
        return ResponseEntity.ok(loyaltyService.addPoints(userId, storeId, amount));
    }

    @PostMapping("/redeem")
    @JsonView(Views.Detalle.class)
    public ResponseEntity<LoyaltyCardDTO> redeem(@RequestBody Map<String, Long> payload) {
        return ResponseEntity.ok(loyaltyService.redeemReward(
                payload.get("userId"),
                payload.get("storeId"),
                payload.get("rewardId")));
    }

    @PutMapping("/update-last-access/{cardId}")
    public void updateLastAccess(@PathVariable Long cardId, @RequestBody String entity) {
        loyaltyService.updateLastAccess(cardId);
    }
}