package tienda.puntos.app.web.webservices;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.annotation.JsonView;
import tienda.puntos.app.model.dto.CompanyDTO;
import tienda.puntos.app.services.Company.CompanyService;
import tienda.puntos.app.utils.Views;

@RestController
@RequestMapping("/api/company")
@CrossOrigin(origins = "*") // Para evitar problemas de CORS en desarrollo
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @GetMapping("/{id}")
    @JsonView(Views.Detalle.class)
    public ResponseEntity<CompanyDTO> findCompanyByID(@PathVariable("id") Long id) {
        return ResponseEntity.ok(companyService.findCompanyByID(id));
    }

    @GetMapping("/from-user/{userId}")
    @JsonView(Views.Detalle.class)
    public ResponseEntity<CompanyDTO> findCompanyFromUserID(@PathVariable Long userId) {
        return ResponseEntity.ok(companyService.findCompanyFromUserID(userId));
    }

    @PostMapping("/cif")
    @JsonView(Views.Detalle.class)
    public ResponseEntity<CompanyDTO> findCompanyByCIF(@RequestBody Map<String, String> json) {
        return ResponseEntity.ok(companyService.findCompanyByCIF(json.get("cif")));
    }

    @PostMapping("/create")
    @JsonView(Views.Detalle.class)
    public ResponseEntity<CompanyDTO> save(@RequestBody CompanyDTO company) {
        return ResponseEntity.ok(companyService.save(company));
    }

    @PutMapping("/update/{companyId}")
    @JsonView(Views.Detalle.class)
    public ResponseEntity<CompanyDTO> update(@PathVariable("companyId") Long cid, @RequestBody CompanyDTO companyDTO) {
        return ResponseEntity.ok(companyService.update(cid, companyDTO));
    }
}