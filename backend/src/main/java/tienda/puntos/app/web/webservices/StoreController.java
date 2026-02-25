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

import tienda.puntos.app.model.dto.StoreDTO;
import tienda.puntos.app.services.Store.StoreService;

@RestController
@RequestMapping("/api/store")
public class StoreController {

    @Autowired
    private StoreService storeService;

    @PreAuthorize("hasRole('ADMIN_PLATAFORMA')")
    @GetMapping("")
    public ResponseEntity<List<StoreDTO>> findAll() {
        return ResponseEntity.ok(this.storeService.findAllStores());
    }

    @GetMapping("/{storeId}")
    public ResponseEntity<StoreDTO> findById(@PathVariable Long storeId) {
        return ResponseEntity.ok(this.storeService.findStoreByID(storeId));
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<StoreDTO>> findAllByCompanyId(@PathVariable Long companyId) {
        // Utilizaremos company ID como representante del owner

        return ResponseEntity.ok(this.storeService.findAllByCompanyId(companyId));
    }

    @PostMapping("/create")
    public ResponseEntity<StoreDTO> save(@RequestBody StoreDTO storeDTO) {
        return ResponseEntity.ok(this.storeService.save(storeDTO));
    }

    @PutMapping("/update/{storeId}")
    public ResponseEntity<StoreDTO> update(@PathVariable Long storeId, @RequestBody StoreDTO storeDTO) {
        return ResponseEntity.ok(this.storeService.update(storeId, storeDTO));
    }

    @PreAuthorize("hasRole('ADMIN_PLATAFORMA') or hasRole('ADMIN_NEGOCIO')")
    @GetMapping("/disable/{storeId}")
    public void disable(@PathVariable Long storeId) {
        this.storeService.disable(storeId);
    }

    @PreAuthorize("hasRole('ADMIN_PLATAFORMA') or hasRole('ADMIN_NEGOCIO')")
    @GetMapping("/activate/{storeId}")
    public void activate(@PathVariable Long storeId) {
        this.storeService.activate(storeId);
    }

}
