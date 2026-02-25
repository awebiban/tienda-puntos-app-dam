package tienda.puntos.app.web.webservices;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import tienda.puntos.app.model.dto.StoreDTO;
import tienda.puntos.app.services.store.StoreService;
import tienda.puntos.app.utils.Views;

@RestController
@RequestMapping("/api/store")
@CrossOrigin(origins = { "http://localhost:4200", "https://ruta66.synology.me" })
public class StoreController {

    @Autowired
    private StoreService storeService;

    @JsonView(Views.Detalle.class)
    @GetMapping("")
    public ResponseEntity<List<StoreDTO>> findAll() {
        return ResponseEntity.ok(this.storeService.findAllStores());
    }

    @GetMapping("/{storeId}")
    @JsonView(Views.Detalle.class)
    public ResponseEntity<StoreDTO> findById(@PathVariable("storeId") Long s) {
        return ResponseEntity.ok(this.storeService.findStoreByID(s));
    }

    @GetMapping("/company/{companyId}")
    @JsonView(Views.Detalle.class)
    public ResponseEntity<List<StoreDTO>> findAllByCompanyId(@PathVariable("companyId") Long c) {
        // Utilizaremos company ID como representante del owner

        return ResponseEntity.ok(this.storeService.findAllByCompanyId(c));
    }

    @PostMapping("/create")
    @JsonView(Views.Detalle.class)

    public ResponseEntity<StoreDTO> save(@RequestBody StoreDTO s) {
        return ResponseEntity.ok(this.storeService.save(s));
    }

    @PutMapping("/update/{storeId}")
    @JsonView(Views.Detalle.class)
    public ResponseEntity<StoreDTO> update(@PathVariable("storeId") Long s, @RequestBody StoreDTO storeDTO) {
        return ResponseEntity.ok(this.storeService.update(s, storeDTO));
    }

    @PreAuthorize("hasRole('ADMIN_PLATAFORMA') or hasRole('ADMIN_NEGOCIO')")
    @GetMapping("/disable/{storeId}")
    @JsonView(Views.Detalle.class)
    public void disable(@PathVariable("storeId") Long s) {
        this.storeService.disable(s);
    }

    @PreAuthorize("hasRole('ADMIN_PLATAFORMA') or hasRole('ADMIN_NEGOCIO')")
    @GetMapping("/activate/{storeId}")
    @JsonView(Views.Detalle.class)
    public void activate(@PathVariable("storeId") Long s) {
        this.storeService.activate(s);
    }

}
