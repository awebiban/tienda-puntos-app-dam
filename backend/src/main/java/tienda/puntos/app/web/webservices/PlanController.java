package tienda.puntos.app.web.webservices;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tienda.puntos.app.model.dto.PlanDTO;
import tienda.puntos.app.services.plan.PlanService;

@RestController
@RequestMapping("/api/plans")
public class PlanController {

    @Autowired
    private PlanService planService;

    @GetMapping("")
    public ResponseEntity<List<PlanDTO>> findAll() {
        return ResponseEntity.ok(this.planService.findAll());
    }

    @PostMapping("")
    public ResponseEntity<PlanDTO> save(@RequestBody PlanDTO plan) {
        return ResponseEntity.ok(this.planService.save(plan));
    }

    @PreAuthorize("hasRole('ADMIN_PLATAFORMA')")
    @GetMapping("/disable/{planId}")
    public void disable(@PathVariable int planId) {
        this.planService.disable(planId);
    }

    @PreAuthorize("hasRole('ADMIN_PLATAFORMA')")
    @GetMapping("/active/{planId}")
    public void active(@PathVariable int planId) {
        this.planService.active(planId);
    }

}
