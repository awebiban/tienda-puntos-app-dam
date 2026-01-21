package tienda.puntos.app.repository.dao;

import java.sql.Date;
import java.time.LocalDateTime;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tienda.puntos.app.repository.entity.Company;
import tienda.puntos.app.utils.SubscriptionStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDTO {

    private Long id;
    private UserDTO ownerDTO; // Usuario dueño de compañia
    private PlanDTO planDTO;
    private String legalName; // Razón Social
    private String cif; // CIF o NIF

    @Enumerated(EnumType.STRING)
    private SubscriptionStatus SubscriptionStatus; // ACTIVE, PAST_DUE, CANCELLED;
    private LocalDateTime nextBillingDate; // En principio se actualiza en bbdd

    public static CompanyDTO convertToDTO(Company c1) {

        CompanyDTO c2 = new CompanyDTO();
        c2.setId(c1.getId());
        c2.setOwnerDTO(UserDTO.convertoToDTO(c1.getOwner()));
        c2.setPlanDTO(PlanDTO.convertToDTO(c1.getPlan()));
        c2.setLegalName(c1.getLegalName());
        c2.setCif(c1.getCif());
        c2.setSubscriptionStatus(c1.getSubscriptionStatus());
        c2.setNextBillingDate(c1.getNextBillingDate());

        return c2;
    }

    public static Company convertToEntity(CompanyDTO c1) {

        Company c2 = new Company();
        c2.setId(c1.getId());
        c2.setOwner(UserDTO.convertoToEntity(c1.getOwnerDTO()));
        c2.setPlan(PlanDTO.convertToEntity(c1.getPlanDTO()));
        c2.setLegalName(c1.getLegalName());
        c2.setCif(c1.getCif());
        c2.setSubscriptionStatus(c1.getSubscriptionStatus());
        c2.setNextBillingDate(c1.getNextBillingDate());

        return c2;
    }

}
