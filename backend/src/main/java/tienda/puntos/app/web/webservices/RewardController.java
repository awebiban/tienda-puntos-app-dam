package tienda.puntos.app.web.webservices;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tienda.puntos.app.model.dto.RewardDTO;
import tienda.puntos.app.services.reward.RewardService;

@RestController
@RequestMapping("/api/rewards")
public class RewardController {

    @Autowired
    private RewardService rewardService;

    @PreAuthorize("hasRole('ADMIN_PLATAFORMA')")
    @GetMapping("")
    public ResponseEntity<List<RewardDTO>> findAll() {
        return ResponseEntity.ok(this.rewardService.findAllRewards());
    }

    @GetMapping("/{rewardId}")
    public ResponseEntity<RewardDTO> findById(@PathVariable Long rewardId) {
        return ResponseEntity.ok(this.rewardService.findRewardById(rewardId));
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<RewardDTO>> findByStore(@PathVariable Long storeId) {
        return ResponseEntity.ok(this.rewardService.findRewardsByStore(storeId));
    }

    @GetMapping("/active/{rewardId}")
    public void findActiveById(@PathVariable Long rewardId) {
        this.rewardService.activeReward(rewardId);
    }

    @GetMapping("/disable/{rewardId}")
    public void disableReward(@PathVariable Long rewardId) {
        this.rewardService.disableReward(rewardId);
    }

    @PostMapping("/create")
    public ResponseEntity<RewardDTO> save(@RequestBody RewardDTO rewardDTO) {
        return ResponseEntity.ok(this.rewardService.saveReward(rewardDTO));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<RewardDTO> update(@PathVariable Long id, @RequestBody RewardDTO rewardDTO) {
        return ResponseEntity.ok(this.rewardService.updateReward(id, rewardDTO));
    }

}
