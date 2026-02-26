package tienda.puntos.app.web.webservices;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
public class LoyaltyController {

    @Autowired
    private LoyaltyCardService loyaltyService;

    @GetMapping("/user/{userId}")
    @JsonView(Views.Resumen.class)
    public ResponseEntity<List<LoyaltyCardDTO>> getMyCards(@PathVariable("userId") Long uid) {
        return ResponseEntity.ok(loyaltyService.getCardsByUser(uid));
    }

    @GetMapping("/history/{cardId}")
    @JsonView(Views.Detalle.class)
    public ResponseEntity<List<TransactionDTO>> getHistory(@PathVariable("cardId") Long cid) {
        return ResponseEntity.ok(loyaltyService.getHistory(cid));
    }

    @PostMapping("/add-points")
    @PreAuthorize("hasRole('ADMIN_NEGOCIO') or hasRole('ADMIN_PLATAFORMA')")
    @JsonView(Views.Detalle.class)
    public ResponseEntity<LoyaltyCardDTO> addPoints(@RequestBody Map<String, Object> payload) {
<<<<<<< Updated upstream
        Long userId = Long.valueOf(payload.get("userId").toString());
        Long storeId = Long.valueOf(payload.get("storeId").toString());
        int amount = Integer.parseInt(payload.get("amount").toString());

        return ResponseEntity.ok(loyaltyService.addPoints(userId, storeId, amount));
=======
        Long cardId = Long.valueOf(payload.get("c").toString());
        Long storeId = Long.valueOf(payload.get("s").toString());
        Long userId = Long.valueOf(payload.get("u").toString());
        int points = Integer.parseInt(payload.get("points").toString());

        return ResponseEntity.ok(loyaltyService.addPointsToCard(cardId, storeId, userId, points));
>>>>>>> Stashed changes
    }

    @PostMapping("/redeem")
    @JsonView(Views.Detalle.class)
    public ResponseEntity<LoyaltyCardDTO> redeem(@RequestBody Map<String, Long> payload) {
        return ResponseEntity.ok(loyaltyService.redeemReward(
                payload.get("userId"),
                payload.get("storeId"),
                payload.get("rewardId")));
    }
}