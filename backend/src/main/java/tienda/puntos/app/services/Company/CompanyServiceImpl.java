package tienda.puntos.app.services.company;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import tienda.puntos.app.model.dto.CompanyDTO;
import tienda.puntos.app.model.dto.PlanDTO;
import tienda.puntos.app.repository.dao.CompanyRepository;
import tienda.puntos.app.repository.dao.PlanRepository;
import tienda.puntos.app.repository.dao.UserRepository;
import tienda.puntos.app.repository.entity.Company;
import tienda.puntos.app.repository.entity.Plan;
import tienda.puntos.app.repository.entity.User;
import tienda.puntos.app.utils.Role;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public @Nullable CompanyDTO findCompanyByID(Long id) {
        return CompanyDTO.convertToDTO(this.companyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Error: La compañía no existe")));
    }

    @Override
    public @Nullable CompanyDTO findCompanyFromUserID(Long userId) {
        Company company = this.companyRepository.findCompanyFromUserID(userId)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la compañía con ID: " + userId));

        return CompanyDTO.convertToDTO(company);
    }

    @Override
    public CompanyDTO findCompanyByCIF(String cif) {
        Company company = this.companyRepository.findCompanyByCIF(cif)
                .orElseThrow(() -> new EntityNotFoundException("Error: La compañía con CIF " + cif + " no existe"));

        return CompanyDTO.convertToDTO(company);
    }

    @Override
    @Transactional // Importante: Si falla la compañía, el rol del usuario no cambia
    public CompanyDTO save(CompanyDTO companyDTO) {

        // 1. Buscamos al usuario REAL que ya está en la BD (este tiene el password)
        User ownerEntity = userRepository.findById(companyDTO.getOwnerDTO().getId())
                .orElseThrow(() -> new EntityNotFoundException("Error: El usuario dueño no existe en la BD"));

        // 2. Solo actualizamos el campo que nos interesa (el Rol)
        // El password de 'ownerEntity' permanece intacto y seguro
        ownerEntity.setRole(Role.ADMIN_NEGOCIO);
        userRepository.save(ownerEntity);

        // 3. Buscamos el Plan (mismo concepto: evitar transient value)
        Plan planEntity = planRepository.findById(companyDTO.getPlanDTO().getId())
                .orElseThrow(() -> new EntityNotFoundException("Error: El plan seleccionado no existe"));

        // 4. Creamos la entidad Compañía y le pasamos las entidades que ya "viven" en
        // la BD
        Company companyToSave = CompanyDTO.convertToEntity(companyDTO);
        companyToSave.setOwner(ownerEntity);
        companyToSave.setPlan(planEntity);

        // 5. Guardamos la compañía
        Company savedCompany = companyRepository.save(companyToSave);

        return CompanyDTO.convertToDTO(savedCompany);
    }

    @Override
    public CompanyDTO update(Long companyId, CompanyDTO companyDTO) {
        Company companyToUpdate = this.companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Error la compañia no existe"));

        // Solo se pueden actualizar estos campos (no el owner)
        companyToUpdate.setPlan(PlanDTO.convertToEntity(companyDTO.getPlanDTO()));
        companyToUpdate.setLegalName(companyDTO.getLegalName());
        companyToUpdate.setCif(companyDTO.getCif());
        companyToUpdate.setSubscriptionStatus(companyDTO.getSubscriptionStatus());
        companyToUpdate.setNextBillingDate(companyDTO.getNextBillingDate());

        Company updatedCompany = this.companyRepository.save(companyToUpdate);
        return CompanyDTO.convertToDTO(updatedCompany);
    }

}
