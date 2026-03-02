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

    @PutMapping("/{cardId}")
    public ResponseEntity<LoyaltyCardDTO> updateCard(@PathVariable Long cardId,
            @RequestBody LoyaltyCardDTO cardDTO) {
        return ResponseEntity.ok(this.loyaltyService.updateCard(cardId, cardDTO));
    }


    @GetMapping("/user/{userId}")
    @JsonView(Views.Detalle.class)
    public ResponseEntity<List<LoyaltyCardDTO>> getMyCards(@PathVariable("userId") Long uid) {
        return ResponseEntity.ok(loyaltyService.getCardsByUser(uid));
    }


    @GetMapping("/store/{storeId}")
    @JsonView(Views.Detalle.class)
    public ResponseEntity<List<LoyaltyCardDTO>> getCardsByStore(@PathVariable("storeId") Long sid) {
        return ResponseEntity.ok(loyaltyService.getCardsByStore(sid));
    }


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


    @PostMapping("/add-points")
    @JsonView(Views.Detalle.class)
    public ResponseEntity<LoyaltyCardDTO> addPoints(@RequestBody Map<String, Object> payload) {

        if (payload.containsKey("cardId")) {
            Long cardId = Long.valueOf(payload.get("cardId").toString());
            int points = Integer.parseInt(payload.get("points").toString());
            return ResponseEntity.ok(loyaltyService.addPointsToCard(cardId, points));
        }

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