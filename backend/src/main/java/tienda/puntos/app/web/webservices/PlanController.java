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

import com.fasterxml.jackson.annotation.JsonView;

import tienda.puntos.app.model.dto.PlanDTO;
import tienda.puntos.app.services.plan.PlanService;
import tienda.puntos.app.utils.Views;

@RestController
@RequestMapping("/api/plans")
public class PlanController {

    @Autowired
    private PlanService planService;

    @GetMapping("")
    @JsonView(Views.Detalle.class)
    public ResponseEntity<List<PlanDTO>> findAll() {
        return ResponseEntity.ok(this.planService.findAll());
    }

    @PreAuthorize("hasRole('ADMIN_PLATAFORMA')")
    @JsonView(Views.Detalle.class)
    @PostMapping("")
    public ResponseEntity<PlanDTO> save(@RequestBody PlanDTO plan) {
        return ResponseEntity.ok(this.planService.save(plan));
    }

    @PreAuthorize("hasRole('ADMIN_PLATAFORMA')")
    @JsonView(Views.Detalle.class)
    @GetMapping("/disable/{planId}")
    public void disable(@PathVariable("planId") Long pid) {
        this.planService.disable(pid);
    }

    @PreAuthorize("hasRole('ADMIN_PLATAFORMA')")
    @JsonView(Views.Detalle.class)
    @GetMapping("/active/{planId}")
    public void active(@PathVariable("planId") Long pid) {
        this.planService.active(pid);
    }

}
