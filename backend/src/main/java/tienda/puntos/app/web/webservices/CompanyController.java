package tienda.puntos.app.web.webservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tienda.puntos.app.model.dto.CompanyDTO;
import tienda.puntos.app.services.Company.CompanyService;

@RestController
@RequestMapping("/api/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @GetMapping("/{id}")
    public ResponseEntity<CompanyDTO> findCompanyByID(@PathVariable Long id) {
        return ResponseEntity.ok(companyService.findCompanyByID(id));
    }

    // sustituir y convertir a store, no corresponde este metodo a este repo
    @GetMapping("/from-user/{userId}")
    public ResponseEntity<CompanyDTO> findCompanyFromUserID(@PathVariable Long userId) {
        return ResponseEntity.ok(companyService.findCompanyFromUserID(userId));
    }

    @PostMapping("/create")
    public ResponseEntity<CompanyDTO> save(@RequestBody CompanyDTO company) {
        return ResponseEntity.ok(companyService.save(company));
    }

}
