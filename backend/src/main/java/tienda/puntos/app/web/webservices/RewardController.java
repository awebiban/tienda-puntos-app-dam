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

import com.fasterxml.jackson.annotation.JsonView;

import tienda.puntos.app.model.dto.RewardDTO;
import tienda.puntos.app.services.reward.RewardService;
import tienda.puntos.app.utils.Views;

@RestController
@RequestMapping("/api/rewards")
public class RewardController {

    @Autowired
    private RewardService rewardService;

    @PreAuthorize("hasRole('ADMIN_PLATAFORMA')")
    @JsonView(Views.Detalle.class)
    @GetMapping("")
    public ResponseEntity<List<RewardDTO>> findAll() {
        return ResponseEntity.ok(this.rewardService.findAllRewards());
    }

    @GetMapping("/{rewardId}")
    @JsonView(Views.Resumen.class)
    public ResponseEntity<RewardDTO> findById(@PathVariable("rewardId") Long rid) {
        return ResponseEntity.ok(this.rewardService.findRewardById(rid));
    }

    @GetMapping("/store/{storeId}")
    @JsonView(Views.Detalle.class)
    public ResponseEntity<List<RewardDTO>> findByStore(@PathVariable("storeId") Long sid) {
        return ResponseEntity.ok(this.rewardService.findRewardsByStore(sid));
    }

    @PreAuthorize("hasRole('ADMIN_PLATAFORMA') or hasRole('ADMIN_NEGOCIO')")
    @GetMapping("/active/{rewardId}")
    @JsonView(Views.Detalle.class)
    public void findActiveById(@PathVariable("rewardId") Long rid) {
        this.rewardService.activeReward(rid);
    }

    @PreAuthorize("hasRole('ADMIN_PLATAFORMA') or hasRole('ADMIN_NEGOCIO')")
    @GetMapping("/disable/{rewardId}")
    @JsonView(Views.Detalle.class)
    public void disableReward(@PathVariable("rewardId") Long rid) {
        this.rewardService.disableReward(rid);
    }

    @PostMapping("/create")
    @JsonView(Views.Detalle.class)
    public ResponseEntity<RewardDTO> save(@RequestBody RewardDTO rewardDTO) {
        return ResponseEntity.ok(this.rewardService.saveReward(rewardDTO));
    }

    @PutMapping("/update/{id}")
    @JsonView(Views.Detalle.class)
    public ResponseEntity<RewardDTO> update(@PathVariable("id") Long i, @RequestBody RewardDTO rewardDTO) {
        return ResponseEntity.ok(this.rewardService.updateReward(i, rewardDTO));
    }

}
