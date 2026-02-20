package tienda.puntos.app.web.webservices;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import tienda.puntos.app.model.dto.CompanyDTO;
import tienda.puntos.app.services.company.CompanyService;
import tienda.puntos.app.utils.Views;

@RestController
@RequestMapping("/api/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @GetMapping("/{id}")
    @JsonView(Views.Detalle.class)
    public ResponseEntity<CompanyDTO> findCompanyByID(@PathVariable("id") Long i) {
        return ResponseEntity.ok(companyService.findCompanyByID(i));
    }

    @PostMapping("/cif")
    @JsonView(Views.Detalle.class)
    public ResponseEntity<CompanyDTO> findCompanyByCIF(@RequestBody Map<String, String> json) {
        return ResponseEntity.ok(companyService.findCompanyByCIF(json.get("cif")));
    }

    // sustituir y convertir a findStoreByCompanyID, no corresponde aqui
    // @GetMapping("/from-user/{userId}")
    // public ResponseEntity<CompanyDTO> findCompanyFromUserID(@PathVariable Long
    // userId) {
    // return ResponseEntity.ok(companyService.findCompanyFromUserID(userId));
    // }

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
